### 1.RabbitMQ简介
* 使用erlang语言开发
* RabbitMQ使用AMQP协议
* 在分布式系统中转发消息
* 用于在组件之间解耦
### 2.相关术语描述
* Broker Server
	* 其角色用于维护一条从Prducer到Consumer的路线
* Producer
	* 数据发送方,消息的生产者,将消息发送到Broker Server
	* 一个Message包含两部分,payload（有效载荷）和label（标签）
	* payload是传输的数据
	* label是exchange的名字或者说是一个tag，它描述了payload
	* RabbitMQ是通过label决定把Message发给哪个Consumer
	* AMQP仅仅描述了label，而RabbitMQ决定了如何使用这个label的规则