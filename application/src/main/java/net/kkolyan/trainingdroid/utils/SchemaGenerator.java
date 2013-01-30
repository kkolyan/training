package net.kkolyan.trainingdroid.utils;

import org.simpleframework.xml.*;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * @author nplekhanov
 */
public class SchemaGenerator {
    private static Map<Class,String> simpleTypeMapping = new HashMap<Class, String>();
    static {
        simpleTypeMapping.put(int.class, "xsd:int");
        simpleTypeMapping.put(Integer.class, "xsd:int");
        simpleTypeMapping.put(float.class, "xsd:float");
        simpleTypeMapping.put(String.class, "xsd:string");
        simpleTypeMapping.put(Date.class, "xsd:string");
        simpleTypeMapping.put(File.class, "xsd:string");
        simpleTypeMapping.put(Class.class, "xsd:string");
    }

    private Map<String,Class> roots = new TreeMap<String, Class>();
    private String schema;

    public SchemaGenerator(String schema) {
        this.schema = schema;
    }

    public SchemaGenerator addRoot(Class aClass, String element) {
        roots.put(element, aClass);
        return this;
    }
    
    private String type(Class aClass, Queue<Class> pending) {
        String simpleType = simpleTypeMapping.get(aClass);
        if (simpleType != null) {
            return simpleType;
        }
        pending.offer(aClass);
        return aClass.getSimpleName();
    }

    public void generate(PrintStream out) {

        Set<Class> generated = new HashSet<Class>();
        Queue<Class> pending = new ArrayDeque<Class>();
        
        for (Class aClass: roots.values()) {
            pending.offer(aClass);
        }

        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<xsd:schema\n" +
                "        xmlns=\""+schema+"\"\n" +
                "        xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
                "        targetNamespace=\""+schema+"\"\n" +
                "        elementFormDefault=\"qualified\"\n" +
                "        attributeFormDefault=\"unqualified\">");
        out.println();

        for (Map.Entry<String, Class> root: roots.entrySet()) {
            out.println("    <xsd:element name=\""+root.getKey()+"\" type=\""+type(root.getValue(), pending)+"\"/>");
            out.println();
        }

        while (true) {
            Class aClass = pending.poll();
            if (aClass == null) {
                break;
            }
            if (generated.contains(aClass)) {
                continue;
            }

            out.println("    <xsd:complexType name=\""+aClass.getSimpleName()+"\">");

            if (hasElements(aClass)) {
                out.println("        <xsd:sequence>");
                for (Field field: aClass.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Element.class)) {
                        out.print("            <xsd:element name=\"" + field.getName() + "\"");
                        out.print(" type=\"" + type(field.getType(), pending) + "\"");
                        if (!field.getAnnotation(Element.class).required()) {
                            out.print(" minOccurs=\"0\"");
                        }
                        out.println("/>");
                    }
                    if (field.isAnnotationPresent(ElementList.class)) {
                        ElementList list = field.getAnnotation(ElementList.class);
                        if (list.entry().isEmpty()) {
                            throw new AssertionError(field);
                        }
                        out.print("            <xsd:element name=\"" + list.entry() + "\"");
                        ParameterizedType type = (ParameterizedType) field.getGenericType();
                        out.print(" type=\"" + type((Class) type.getActualTypeArguments()[0], pending) + "\"");
                        out.print(" minOccurs=\"0\"");
                        out.print(" maxOccurs=\"unbounded\"");
                        if (!list.inline()) {
                            throw new AssertionError();
                        }
                        out.println("/>");
                    }
                    if (field.isAnnotationPresent(ElementMap.class)) {
                        throw new AssertionError();
                    }
                    if (field.isAnnotationPresent(Text.class)) {
                        out.print("\n" +
                                "    <xs:restriction base=\"xs:string\">" +
                                "    </xs:restriction>");
                    }
                }
                out.println("        </xsd:sequence>");
            }

            for (Field field: aClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Attribute.class)) {
                    out.print("        <xsd:attribute name=\"" + field.getName() + "\" ");
                    out.print(" type=\"" + type(field.getType(), pending) + "\"");
                    if (field.getAnnotation(Attribute.class).required()) {
                        out.print(" use=\"required\"");
                    }
                    out.println("/>");
                }
            }
            out.println("    </xsd:complexType>");
            out.println();

            generated.add(aClass);
        }
        out.println("</xsd:schema>");
        out.flush();
    }

    private boolean hasElements(Class aClass) {
        for (Field field: aClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Element.class) || field.isAnnotationPresent(ElementList.class)) {
                return true;
            }
        }
        return false;
    }
}
