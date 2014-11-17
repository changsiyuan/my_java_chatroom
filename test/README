# 关于测试程序的说明

测试程序可以实现测试源程序中的功能实现情况

只针对client端编写本测试程序，来测试server端能否返回正确的信息


测试程序放在test包中，一共有三个程序：
TestClientMyself.java：测试了用户chang在登录时错误命令、登录过程、群发消息、给用户si发私聊消息等情况的程序运行是否正确；
TestClientOthers1.java：测试了用户si登陆过程、收到用户chang已经登陆的信息、收到用户chang群发的消息、收到用户chang和自己私聊的消息等情况程序运行是否正确；
TestClientOthers2.java：测试了另外一个用户企图用已经登录的chang用户名登陆的时候，程序是否按照规定提示“用户名已经存在”；


测试的过程：
1.起server程序。
2.起TestClientOthers1.java，使得用户si先登录。
3.起TestClientMyself.java，使得用户chang登录并测试。
4.起TestClientOthers2.java，测试用户名已存在情况下登录。
要严格按照上面的顺序测试。

测试结果分析：
如果测试成功，则显示This test is successful!
如果测试失败（server端没有返回应该返回的信息或返回信息错误），则显示：The output is: " （返回的错误信息） "....it is not true

实测结果：
全部显示测试成功，表明server端已经达到设计要求；
