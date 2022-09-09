这是本人之前编写的后门盗号 mod 的源代码。

原理是劫持聊天包，将发送的登录或注册命令中的密码储存起来，收到“登录成功”的聊天信息后通过邮箱发送玩家名及其密码。登录成功消息仅针对「生存都市」服务器。

本人评价：代码写得很坏，甚至没有用 Mixin 来注入。后门部分几乎全在 Util，~~我那时怎么这么中二而且连 shadow 打包库进去都不会还要带上库的源码编译。~~

公开代码仅供学习参考。本仓库就此走到终点。

# 以下是原始 README

# AnonymousHelper
欢迎使用匿名者工具组
Make by 匿名者科技

## 这是啥?  
匿名者工具组是在Minecraft中的一个辅助mod，  
适用于 Minecraft 1.8.9 Forge 11.15.1.1722+

## 它有啥用?  
**目前它有以下功能**  
> 快速村民兑换  
> 无限夜视  
> 区块显示  
> 修改玩家名称  

更多功能 Comming soon.

## 下载
[Release](https://github.com/AnonymTechnology/AnonymousHelper/releases)  
有更多问题或者建议可以前往 [Issues](https://github.com/AnonymTechnology/AnonymousHelper/issues) 提出

## 开源
本mod**不开放**源代码
 
