# SecureCRT

---

## 1.文件上传和下载
* 使用sftp方式
	* ALT+P ：				开启sftp会话
	* cd xxx：				切换Linux文件上传到的路径
	* put d:/text.txt :  	将本地目录中文件上传到远程主机(linux)
	* get:  				将远程目录中文件下载到本地目录          

	* pwd:  查询linux主机所在目录(也就是远程主机目录)
	* lpwd: 查询本地目录（一般指windows上传文件的目录）
	* ls:   查询连接到当前linux主机所在目录有哪些文件
	* lls:  查询当前本地上传目录有哪些文件
	* lcd:  改变本地上传目录的路径                           
	* help命令，显示该FTP提供所有的命令 
	* quit: 断开FTP连接 
* rz & sz
	* 需要先安装软件支持
		* yum install lrzsz -y
	* rz ： 上传
		* -y 直接上传并覆盖
	* sz ：	下载
		* -y 直接下载并覆盖

## 2.设置CRT可以同时管理多个窗口
#### 2.1 勾选
![](http://i.imgur.com/5ZQb3AY.png)

#### 2.2 在查看中选择 <交互窗口>
#### 2.3 在底部白色区域右键选择<将交互发送到所有标签>
![](http://i.imgur.com/Rw19UC4.jpg)