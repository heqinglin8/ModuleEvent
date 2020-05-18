package com.tt52.moduleevent.compiler;

import com.google.auto.service.AutoService;
import com.tt52.moduleevent.interfaces.annotation.EventType;
import com.tt52.moduleevent.interfaces.annotation.ModuleEvents;
import com.tt52.moduleevent.compiler.bean.EventInfo;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by liaohailiang on 2018/8/30.
 */
@AutoService(Processor.class)
public class SmartEventProcessor extends AbstractProcessor {

    private static final String TAG = "[SmartEventProcessor]";

    private static final String DEFAULT_BUS_NAME = "DefaultSmartEventBus";
    private static final String OBSERVABLE_PACKAGE_NAME = "com.tt52.moduleevent.core";
    private static final String OBSERVABLE_CLASS_NAME = "Observable";
    private static final String EVENTBUS_PACKAGE_NAME = "com.tt52.moduleevent";
    private static final String EVENTBUS_CLASS_NAME = "LiveEventBus";
    private static final String BUSNAME_MARK = "Bus";

    Filer filer;
    Types types;
    Elements elements;
    Messager messager;

    String defaultPackageName = null;
    String packageName = null;
    String moduleName = null;
    String busName = null;
    List<EventInfo> eventInfos = new ArrayList<>();
    List<EventInfo> eventInfos2 = new ArrayList<>();
    boolean isGenerateTargetClass = false;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        types = processingEnvironment.getTypeUtils();
        elements = processingEnvironment.getElementUtils();
        messager = processingEnvironment.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(ModuleEvents.class.getCanonicalName());
        annotations.add(EventType.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println(TAG + "roundEnvironment: " + roundEnvironment);
//        messager.printMessage(Diagnostic.Kind.WARNING,TAG + "roundEnvironment: " + roundEnvironment);
        if (!roundEnvironment.processingOver() && !isGenerateTargetClass) {
            processAnnotations(roundEnvironment);
        }
        return true;
    }

    private void processAnnotations(RoundEnvironment roundEnvironment) {
        for (Element element : roundEnvironment.getElementsAnnotatedWith(ModuleEvents.class)) {
            moduleName = getAnnotation(element, ModuleEvents.class, "moduleName");
            busName = getAnnotation(element, ModuleEvents.class, "busName");
            packageName = getAnnotation(element, ModuleEvents.class, "packageName");

            TypeElement typeElement = (TypeElement) element;
            String className = typeElement.getSimpleName().toString();
            PackageElement packageElement = elements.getPackageOf(element);
            String packageName = packageElement.getQualifiedName().toString();
            if (defaultPackageName == null) {
                defaultPackageName = packageName;
            }
            if(busName == null || "".equals(packageName)){
                busName = className + BUSNAME_MARK;
            }
            eventInfos.clear();

            System.out.println(TAG+"EventType elem"+element.toString()+" getSimpleName:"+element.getSimpleName()+"moduleName："+moduleName+"packageName："+packageName+"busName："+busName);
            if (element.getKind() == ElementKind.CLASS) {
                // print fields
                for (Element enclosedElement : element.getEnclosedElements()) {
                    if (enclosedElement.getKind() == ElementKind.FIELD) {
                        com.sun.tools.javac.code.Type type = getAnnotation(enclosedElement, EventType.class, "value");
                        String value = Object.class.getCanonicalName();
                        if(type!=null){
                            value = type.toString();
                        }
                        EventInfo eventInfo = new EventInfo(enclosedElement.getSimpleName().toString(), value);
                        eventInfos.add(eventInfo);
                        System.out.println(TAG+ " value:"+value);
                    }
                }
                System.out.println(TAG+" eventInfos:"+ eventInfos);
            }
            generateBusCode();
        }
    }

    private <T> T getAnnotation(Element element, Class<? extends Annotation> type, String name) {
        String canonicalName = type.getCanonicalName();
        List<? extends AnnotationMirror> annotationMirrors = elements.getAllAnnotationMirrors(element);
        if (annotationMirrors != null && annotationMirrors.size() > 0) {
            for (AnnotationMirror annotationMirror : annotationMirrors) {
                if (canonicalName.equals(annotationMirror.getAnnotationType().toString())) {
                    if (annotationMirror.getElementValues() != null) {
                        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
                                annotationMirror.getElementValues().entrySet()) {
                            ExecutableElement annotationName = entry.getKey();
                            AnnotationValue annotationValue = entry.getValue();
                            if (annotationName.getSimpleName().toString().equals(name)) {
                                return (T) annotationValue.getValue();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private TypeName getTypeName(String name) {
        if (name == null || name.length() == 0) {
            return null;
        }
        java.lang.reflect.Type type = getType(name);
        if (type != null) {
            return ClassName.get(type);
        } else {
            return TypeVariableName.get(name);
        }
    }

    private Type getType(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private void generateBusCode() {
        if (eventInfos == null || eventInfos.size() == 0) {
            return;
        }
        if (packageName == null || packageName.length() == 0) {
            packageName = defaultPackageName;
        }
        String className = (busName != null && busName.length() > 0) ? busName : DEFAULT_BUS_NAME;
        TypeSpec.Builder builder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Auto generate code, do not modify!!!");
        for (EventInfo eventInfo : eventInfos) {
            //添加每一个方法
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(eventInfo.getKey())
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
            //添加return
            ClassName baseClassName = ClassName.get(OBSERVABLE_PACKAGE_NAME, OBSERVABLE_CLASS_NAME);
            TypeName typeName = getTypeName(eventInfo.getType());
            TypeName returnType = ParameterizedTypeName.get(baseClassName, typeName);
            methodBuilder.returns(returnType);
            //添加方法体
            ClassName lebClass = ClassName.get(EVENTBUS_PACKAGE_NAME, EVENTBUS_CLASS_NAME);
            methodBuilder.addStatement("return $T.get($S, $S, $T.class)",
                    lebClass, moduleName, eventInfo.getKey(), typeName);

            builder.addMethod(methodBuilder.build());
        }

        TypeSpec typeSpec = builder.build();
        try {
            JavaFile.builder(packageName, typeSpec)
                    .build()
                    .writeTo(filer);
            isGenerateTargetClass = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
