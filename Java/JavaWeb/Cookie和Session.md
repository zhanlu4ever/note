### 1.Cookie和Session简介
* Cookie通过在客户端记录信息确定用户身份
* Session通过在服务器端记录信息确定用户身份
* Cookie机制
	* Cookie技术是客户端的解决方案
	* Cookie是由服务器发给客户端的特殊信息
	* 信息以文本文件的方式存放在客户端
	* 客户端每次向服务器发送请求的时候都会带上这些特殊的信息
	* Cookie信息是存放于HTTP响应头（Response Header）非响应体
	* Cookie发送信息到服务器是将信息放在HTTP请求头（Request Header）
	* 负责设置cookie的两个HTTP请求头字段是Set-Cookie和Cookie
		* 服务器返回给客户端一个http响应信息时如果包含Set-Cookie这个头部,代表要客户端建立一个cookie,并在后续的http请求中自动发送这个cookie到服务器,直到这个cookie过期
		* 若cookie的生存时间是整个会话期间,那么浏览器会将cookie保存在内存中,浏览器关闭时就会自动清除这个cookie
		* 若将cookie保存在客户端的硬盘中,浏览器关闭后,该cookie不会被清除,下次打开浏览器访问对应网站时，这个cookie会自动再次发送到服务器端
			
			![](http://i.imgur.com/b2Zz5P2.png)
	
	* Java对Cookie的支持
		* javax.servlet.http.Cookie
			* request.getCookie(),获取客户端提交的所有Cookie,以Cookie[]数组形式返回
			* response.addCookie(Cookiecookie)向客户端设置Cookie
			* cookie.setMaxAge(Integer.MAX_VALUE)  设置cookie有效期
			* 修改cookie,新建一个同名的Cookie,添加到response中覆盖原来的Cookie
			* 删除cookie,新建一个同名的Cookie,并将maxAge设置为0,添加到response中覆盖原来的Cookie
			* 修改,删除Cookie时,新建的Cookie除value,maxAge之外的所有属性,如name,path,domain等,都要与原Cookie完全一样.否则,浏览器将视为两个不同的Cookie不予覆盖,导致修改,删除失败
			* 若想所有一级域名下的二级域名都可以使用Cookie,需要设置Cookie的domain参数
			
					Cookie cookie = new Cookie("time","20080808"); // 新建Cookie
					cookie.setDomain(".cbooy.com"); // 设置域名
					cookie.setPath("/"); // 设置路径
					cookie.setMaxAge(Integer.MAX_VALUE); // 设置有效期
					response.addCookie(cookie); // 输出到客户端
			* cookie.setPath("/session/"); // 设置路径
			* cookie.setSecure(true); // 设置安全属性
	* Cookie使用key-value属性对的形式保存用户状态,一个Cookie对象保存一个属性对,一个request或者response同时使用多个Cookie
	* Cookie具有不可跨域名性,即使相同的网站域名不同也不能互相操作cookie
### 2.Session
* Java对Session的支持
	* javax.servlet.http.HttpSession
	* 读取客户端session , request.getSession()
		* getSession(boolean create)
		* 如果该客户的Session不存在,request.getSession()方法会返回null,而getSession(true)会先创建Session再将Session返回
	* 读信息,getAttribute(Stringkey)
	* 写信息,setAttribute(String key，Objectvalue)
* Session的生命周期
	* Session保存在服务器端
	* 为了获得更高的存取速度,服务器一般把Session放在内存里
	* 每个用户都会有一个独立的Session
	* 若Session内容过于复杂,当大量客户访问服务器时可能会导致内存溢出，因此，Session里的信息应该尽量精简
	* Session生成后,只要用户继续访问,服务器就会更新Session的最后访问时间,并维护该Session.
	* 用户每访问服务器一次,无论是否读写Session,服务器都认为该用户的Session"活跃（active）"了一次
* Session的有效期
	* Session的超时时间为maxInactiveInterval属性
	* getMaxInactiveInterval()	获取
	* setMaxInactiveInterval(longinterval)	设置
	* Session的超时时间也可以在web.xml中修改
	* Session的invalidate()方法可以使Session失效
	* Tomcat中Session的默认超时时间为20分钟,通过setMaxInactiveInterval(int seconds)修改超时时间,也可以修改web.xml改变Session的默认超时时间例如修改为60分钟
		
			<session-config>
			   <session-timeout>60</session-timeout>      <!-- 单位：分钟 -->
			</session-config>
		* <session-timeout>参数的单位为分钟,而setMaxInactiveInterval(int s)单位为秒
		* server.xml中定义context时采用如下定义（单位为秒）
		
				<Context path="/livsorder" docBase="/home/httpd/html/livsorder" defaultSessionTimeOut="3600" isWARExpanded="true" isWARValidated="false" isInvokerEnabled="true" isWorkDirPersistent="false"/>
* Session的常用方法
	
		void setAttribute(String attribute, Object value)：设置Session属性。value参数可以为任何Java Object。通常为Java Bean。value信息不宜过大 

		String getAttribute(String attribute)：返回Session属性 

		Enumeration getAttributeNames()：返回Session中存在的属性名 

		void removeAttribute(String attribute)：移除Session属性 

		String getId()：返回Session的ID。该ID由服务器自动创建，不会重复 long 

		getCreationTime()：返回Session的创建日期。返回类型为long，常被转化为Date类型，例如：Date createTime = new Date(session.get CreationTime()) 

		long getLastAccessedTime()：返回Session的最后活跃时间。返回类型为long 

		int getMaxInactiveInterval()：返回Session的超时时间。单位为秒。超过该时间没有访问，服务器认为该Session失效 

		void setMaxInactiveInterval(int second)：设置Session的超时时间。单位为秒 

		void putValue(String attribute, Object value)：不推荐的方法。已经被setAttribute(String attribute, Object Value)替代 

		Object getValue(String attribute)：不被推荐的方法。已经被getAttribute(String attr)替代 

		boolean isNew()：返回该Session是否是新创建的 

		void invalidate()：使该Session失效	
* Session与浏览器的交互
	* Session需要使用Cookie作为识别标志
	* 服务器向客户端浏览器发送一个名为JSESSIONID的Cookie,它的值为该Session的id（也就是HttpSession.getId()的返回值）.Session依据该Cookie来识别是否为同一用户

### 3.Cookie与Session的区别
* cookie数据存放在客户的浏览器上,session数据放在服务器上
* cookie不是很安全,别人可以分析存放在本地的COOKIE并进行COOKIE欺骗,考虑到安全应当使用session
* session会在一定时间内保存在服务器上,当访问增多,会比较占用你服务器的性能.考虑到减轻服务器性能方面,应当使用COOKIE
* 单个cookie在客户端的限制是3K,就是说一个站点在客户端存放的COOKIE不能超过3K