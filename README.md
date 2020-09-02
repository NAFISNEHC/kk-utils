### kkutils kk的工具包

### 使用说明

先把项目clone到本地，然后将项目打包，并同步到本地的maven中去

> 升级版本的命令：mvn versions:set -DnewVersion=1.0.2
> 执行命令（不带注释的打包）：mvn install  
> 执行命令（带方法注释）：mvn clean source:jar install -DskipTests　
  
安装到本地

> mvn install:install-file -Dfile=./target/kk.utils-1.0.6.jar -DgroupId=com.kk.base -DartifactId=theling.utils -Dversion=1.0.6 -Dpackaging=jar


#### 里面包含以下一些方法

##### AppDateUtil 时间操作

##### BuildTree 树的构建

##### ExcelZrUtils Excel的工具

##### FileUtil 文件的操作工具

关于resources文件夹下的公私钥生成方式

```
第一步：生成私钥，这里我们指定私钥的长度为2048
openssl genrsa -out rsa_private_key.pem 2048

第二步：根据私钥生成对应的公钥：下面两个都可以
1: openssl rsa -in rsa_private_key.pem -pubout -out rsa_public_key.pem
2: openssl rsa -in rsa_private_key.pem -pubout -out rsa_public_key_2048.pub 
第三步：私钥转化成pkcs8格式，【这一步非必须，只是程序解析起来方便】，尖括号的意思是：将转化好的私钥写到rsa_private_key_pkcs8.pem文件里
openssl pkcs8 -topk8 -inform PEM -in rsa_private_key.pem -outform PEM -nocrypt > rsa_private_key_pkcs8.pem
```

> 注意：生成的pem文件，在java中使用，需要去掉头尾，不然会报错