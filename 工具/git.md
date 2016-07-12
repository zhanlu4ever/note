# Git 

---
**廖雪峰Git教程**	[http://www.liaoxuefeng.com/wiki/0013739516305929606dd18361248578c67b8067c8c017b000](http://www.liaoxuefeng.com/wiki/0013739516305929606dd18361248578c67b8067c8c017b000)

**看日记学git by Linux 大棚**	[http://roclinux.cn/?page_id=3775](http://roclinux.cn/?page_id=3775)

**闯过这 54 关，点亮你的 Git 技能树**	[http://www.kuqin.com/shuoit/20160105/349807.html](http://www.kuqin.com/shuoit/20160105/349807.html)

---

## 1.git分支和master对比
> git diff branch-name origin/master

> git diff branch-name origin/master --color > D:/diff.txt

## 2.git 配置
* 查看git配置
	> git config --list
* 配置git用户名和email
	> git config --global user.name xyz

	> git config --global user.email xyz@xyz.com

## 3.生成ssh密钥
> ssh-keygen -t rsa -C "xyz@zyx.com"

## 4.显示某文件的修改历史
* 显示全部log
	> git log
* 只显示commit id
	> git log --pretty=oneline file_name

## 5.查看某一次提交的修改
> git show 7939cdbff8d7622167c3708e9d9a182ff301422d 

## 6.回退commit
* 结合log可以回退到某一次提交上
	> git log

	> git reset --hard HEAD^
* git中使用HEAD表示当前版本，上一个版本是HEAD^ ，上上一个版本是HEAD^^ ，前n个版本是HEAD~N
* 回退之后在回到最新的版本上,找到最新版的id即可回去(不能关闭终端)
	> git reset --hard commit_id
* 关闭终端后找不到原来的head,再次打开之后使用git reflog查看原来的操作记录
  ![](http://i.imgur.com/NAnsJMg.png)