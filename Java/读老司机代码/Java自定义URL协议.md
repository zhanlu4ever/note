# Java自定义URL协议

## 参考资料

[http://blog.csdn.net/pipisky2006/article/details/6321411](http://blog.csdn.net/pipisky2006/article/details/6321411)

---

## 具体代码实现
	// 调用处
	static {
    	URL.setURLStreamHandlerFactory(new ConfigurableURLStreamHandlerFactory());
  	}

---

	public class ConfigurableURLStreamHandlerFactory implements java.net.URLStreamHandlerFactory {
	
	    private final Map<String, URLStreamHandler> handlers = new HashMap<String, URLStreamHandler>();
	
	    public ConfigurableURLStreamHandlerFactory() {
	        register("file", new sun.net.www.protocol.file.Handler());
	        register("classpath", new ClasspathHandler());
	        register("jar", new sun.net.www.protocol.jar.Handler());
	    }
	
	    public ConfigurableURLStreamHandlerFactory register(String protocol, URLStreamHandler urlStreamHandler) {
	        handlers.put(protocol, urlStreamHandler);
	        return this;
	    }
	
	
	    @Override
	    public URLStreamHandler createURLStreamHandler(String protocol) {
	        URLStreamHandler handler = handlers.get(protocol);
	        if (handler == null) {
	            throw new UnsupportedOperationException();
	        }
	        return handler;
	    }
	}

---

	public class ClasspathHandler extends URLStreamHandler {
	
	    private final ClassLoader classLoader;
	
	    public ClasspathHandler() {
	        this.classLoader = getClass().getClassLoader();
	    }
	
	    public ClasspathHandler(ClassLoader classLoader) {
	        this.classLoader = classLoader;
	    }
	
	    @Override
	    protected URLConnection openConnection(URL url) throws IOException {
	        final URL resourceUrl = classLoader.getResource(url.getPath());
	        return resourceUrl.openConnection();
	    }
	}