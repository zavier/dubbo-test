package com.zavier.dubbotest.model;

import com.google.common.base.Splitter;
import com.zavier.dubbotest.common.BusinessException;
import javassist.bytecode.ClassFile;
import javassist.bytecode.MethodInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Class detail.
 *
 * @date 2020-01-15 12:40
 * @author zhengwei20
 */
@Slf4j
@Data
public class ClassDetail {

    /**
     * The Class file.
     */
    private ClassFile classFile;

    /**
     * Gets methods.
     *
     * @return the methods
     */
    public List<ClassMethod> getMethods() {
        if (classFile == null) {
            log.error("class file is null");
            throw new IllegalArgumentException("classFile不能为空");
        }
        List<MethodInfo> methods = classFile.getMethods();

        List<ClassMethod> serviceMethods = new ArrayList<>();
        for (MethodInfo methodInfo : methods) {
            ClassMethod serviceMethod = new ClassMethod();
            serviceMethod.setClassName(classFile.getName());

            String methodName = methodInfo.getName();
            serviceMethod.setMethodName(methodName);

            String descriptor = methodInfo.getDescriptor();
            List<String> parameterTypes = getParameterTypes(descriptor);
            List<ParamPair> paramPairs = parameterTypes.stream().map(t -> new ParamPair("", t)).collect(Collectors.toList());
            serviceMethod.setParamInfoList(paramPairs);

            serviceMethods.add(serviceMethod);
        }
        return serviceMethods;
    }

    /**
     * Gets parameter types.
     *
     * @param descriptor the descriptor
     * @return the parameter types
     */
    private List<String> getParameterTypes(String descriptor) {
        String typeStr = descriptor.substring(descriptor.indexOf("(") + 1, descriptor.lastIndexOf(")"));
        List<String> types = Splitter.on(";").omitEmptyStrings().trimResults().splitToList(typeStr);
        List<String> parameterTypes = new ArrayList<>();
        for (String type : types) {
            String typeTag = type.substring(0, 1);
            switch (typeTag) {
                // object type
                case "L":
                    parameterTypes.add(type.substring(1).replace("/", "."));
                    break;
                case "B":
                    parameterTypes.add("byte");
                    break;
                case "C":
                    parameterTypes.add("char");
                    break;
                case "D":
                    parameterTypes.add("double");
                    break;
                case "F":
                    parameterTypes.add("float");
                    break;
                case "I":
                    parameterTypes.add("int");
                    break;
                case "J":
                    parameterTypes.add("long");
                    break;
                case "S":
                    parameterTypes.add("short");
                    break;
                case "Z":
                    parameterTypes.add("boolean");
                    break;
                case "V":
                    parameterTypes.add("void");
                    break;
                case "[":
                    parameterTypes.add(type.substring(1).replace("/", ".") + "[]");
                    break;
                    default:
                        throw new BusinessException("不支持的类型");
            }
        }
        return parameterTypes;
    }
}
