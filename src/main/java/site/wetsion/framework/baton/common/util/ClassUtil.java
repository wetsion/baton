package site.wetsion.framework.baton.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * ClassUtil
 *
 * @author <a href="mailto:weixin@cai-inc.com">霜华</a>
 * @date 2020/12/12 10:55 PM
 **/
@Slf4j
public class ClassUtil {

    private static final String PROTOCOL_TYPE_FILE = "file";

    private static final String PROTOCOL_TYPE_JAR = "jar";


    public static List<Class<?>> getAllClassByInterface(Class clazz) {
        List<Class<?>> list = new ArrayList<>();
        // 判断是否是一个接口
        if (clazz.isInterface()) {
            try {
                ArrayList<Class> allClass = getAllClass(clazz.getPackage().getName());
                // 循环判断路径下的所有类是否实现了指定的接口 并且排除接口类自己
                for (Class clz : allClass) {
                    // 判断是不是同一个接口
                    // isAssignableFrom:判定此 Class 对象所表示的类或接口与指定的 Class
                    // 参数所表示的类或接口是否相同，或是否是其超类或超接口
                    if (clazz.isAssignableFrom(clz)) {
                        if (!clazz.equals(clz)) {
                            // 自身并不加进去
                            list.add(clz);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("获取包下所有类失败", e);
                throw e;
            }
        }
        log.info("class list size :"+list.size());
        return list;
    }


    /**
     * 从一个指定路径下查找所有的类
     *
     * @param packageName 包
     */
    private static ArrayList<Class> getAllClass(String packageName) {


        log.info("search package：{}", packageName);
        List<String> classNameList =  getClassName(packageName);
        ArrayList<Class> list = new ArrayList<>();

        for(String className : classNameList){
            try {
                list.add(Class.forName(className));
            } catch (ClassNotFoundException e) {
                log.error("load class from name failed: {}", className);
                throw new RuntimeException(e);
            }
        }
        log.info("found class list size : {}", list.size());
        return list;
    }

    /**
     * 获取某包下所有类
     * @param packageName 包名
     * @return 类的完整名称
     */
    public static List<String> getClassName(String packageName) {

        List<String> fileNames = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace(".", File.separator);
        URL url = loader.getResource(packagePath);
        if (url != null) {
            String type = url.getProtocol();
            log.debug("file type : {} ", type);
            if (PROTOCOL_TYPE_FILE.equals(type)) {
                String fileSearchPath = url.getPath();
                log.debug("fileSearchPath: {}", fileSearchPath);
                fileSearchPath = fileSearchPath.substring(0, fileSearchPath.indexOf(File.separator + "classes"));
                log.debug("fileSearchPath: {}", fileSearchPath);
                fileNames = getClassNameByFile(fileSearchPath, fileSearchPath);
            } else if (PROTOCOL_TYPE_JAR.equals(type)) {
                try{
                    JarURLConnection urlConnection = (JarURLConnection)url.openConnection();
                    JarFile jarFile = urlConnection.getJarFile();
                    fileNames = getClassNameByJar(jarFile);
                }catch (IOException e){
                    log.error("open Package URL failed");
                    throw new RuntimeException(e);
                }

            }else{
                throw new RuntimeException("file system not support!cannot load!");
            }
        }
        return fileNames;
    }

    /**
     * 从项目文件获取某包下所有类
     * @param filePath 文件路径
     * @return 类的完整名称
     */
    private static List<String> getClassNameByFile(final String filePath, final String originTargetPath) {
        List<String> myClassName = new ArrayList<>();
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        assert childFiles != null;
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                myClassName.addAll(getClassNameByFile(childFile.getPath(), originTargetPath));
            } else {
                String childFilePath = childFile.getPath();
                if (childFilePath.endsWith(".class")) {
                    childFilePath = childFilePath.replace(originTargetPath + File.separator, "");
                    childFilePath = childFilePath.substring(childFilePath.indexOf(File.separator) + 1,
                            childFilePath.lastIndexOf("."));
                    childFilePath = childFilePath.replace(File.separator, ".");
                    myClassName.add(childFilePath);
                }
            }
        }

        return myClassName;
    }

    /**
     * 从jar获取某包下所有类
     * @return 类的完整名称
     */
    private static List<String> getClassNameByJar(JarFile jarFile) {
        List<String> classNames = new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String entryName = jarEntry.getName();
            if (entryName.endsWith(".class")) {
                entryName = entryName.replace(File.separator, ".").substring(0, entryName.lastIndexOf("."));
                classNames.add(entryName);
            }
        }
        return classNames;
    }
}
