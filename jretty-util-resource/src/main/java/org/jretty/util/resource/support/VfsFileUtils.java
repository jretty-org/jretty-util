package org.jretty.util.resource.support;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

public class VfsFileUtils {
    
    
    public static Set<String> listAllResources(URL rootURL) throws IOException {
        Object root = VfsPatternUtils.findRoot(rootURL);
        VirtualFileVisitor visitor =
                new VirtualFileVisitor();
        VfsPatternUtils.visit(root, visitor);
        System.out.println("=========----------------=========="+visitor);
        return visitor.getResourcesPath();
    }
    
    private static class VirtualFileVisitor implements InvocationHandler {

        //private final Set<Object> resources = new LinkedHashSet<Object>();
        private final Set<String> resourcesPath = new LinkedHashSet<String>();

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            if (Object.class.equals(method.getDeclaringClass())) {
                if (methodName.equals("equals")) {
                    // Only consider equal when proxies are identical.
                    return (proxy == args[0]);
                }
                else if (methodName.equals("hashCode")) {
                    return System.identityHashCode(proxy);
                }
            }
            else if ("getAttributes".equals(methodName)) {
                return getAttributes();
            }
            else if ("visit".equals(methodName)) {
                visit(args[0]);
                return null;
            }
            else if ("toString".equals(methodName)) {
                return toString();
            }
            
            throw new IllegalStateException("Unexpected method invocation: " + method);
        }

        public void visit(Object vfsResource) {
            //this.resources.add(vfsResource);
            
            this.resourcesPath.add(VfsPatternUtils.getPath(vfsResource));
        }

        public Object getAttributes() {
            return VfsPatternUtils.getVisitorAttribute();
        }

//        public Set<Object> getResources() {
//            return this.resources;
//        }
        
        public Set<String> getResourcesPath() {
            return this.resourcesPath;
        }

        @SuppressWarnings("unused")
        public int size() {
            return this.resourcesPath.size();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("resources: ").append(this.resourcesPath);
            return sb.toString();
        }
        
    }

}
