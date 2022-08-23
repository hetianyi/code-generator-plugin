# Java代码生成插件

## 简介

该插件可以很方便地将数据库中的表转为```Java POJOs```

同时还能将通用场景下数据库表自动生成对应的增删改查接口代码（需要模版代码）。
> 参考template文件夹下的模版代码


## 数据库Schema转POJOs

### 配置和使用


在```pom.xml```引入插件


```xml
<plugin>
	<groupId>com.github.hetianyi</groupId>
	<artifactId>code-generator-plugin</artifactId>
	<version>1.0.0-SNAPSHOT</version>
	<configuration>
		<!-- MySQL数据库配置信息 -->
		<database>
			<host>127.0.0.1</host>
			<port>3306</port>
			<dbName>test</dbName>
			<username>root</username>
			<password>123456</password>
		</database>
		<!-- Postgresql数据库配置信息 -->
		<!--<database>
			<host>192.168.0.100</host>
			<port>5432</port>
			<dbName>test</dbName>
			<type>postgresql</type>
			<schema>public</schema>
			<username>admin</username>
			<password>123456</password>
		</database>-->

		<!-- 激活的profile -->
		<activeProfiles>
			<value>po</value>
		</activeProfiles>

		<!-- profile列表 -->
		<profiles>
			<profile>
				<!-- profile名称 -->
				<name>po</name>
				<!-- 输出文件夹 -->
				<outputDir>./generated</outputDir>
				<!-- 生成类的包名 -->
				<packageName>com.example</packageName>
				<!-- 生成类的作者 -->
				<author>Jason He</author>
				<!-- 生成类的前缀 -->
				<appendPrefix>Create</appendPrefix>
				<!-- 生成类的后缀 -->
				<appendSuffix>PO</appendSuffix>
				<!-- 换行符：CR|LF|CRLF, 默认CRLF -->
				<lineSeparator>CRLF</lineSeparator>
				<!-- 生成类的版权信息 -->
				<copyright>
Copyright (c) 1994, 2013, Oracle and/or its affiliates. All rights reserved.
ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
				</copyright>
				<!-- 只生成指定表的类，不指定默认生成所有表的类 -->
				<includeTables>
					<!--<value>t_user</value>-->
				</includeTables>
				<!-- 增强特性 -->
				<features>
					<value>LombokDataAnnotationFeature</value>
					<value>MyBatisPlusClassAnnotationFeature</value>
					<value>MyBatisPlusFieldAnnotationFeature</value>
					<value>-GetterSetterFeature</value>
					<value>com.github.hetianyi.learn.demo.config.Slf4jFeature</value>
					<values>JasonFormatAnnotationFeature</values>
					<values>LombokAllArgsConstructorAnnotationFeature</values>
					<values>LombokBuilderAnnotationFeature</values>
					<values>LombokNoArgsConstructorAnnotationFeature</values>
					<values>ConvertibleFeature</values>
					<values>SwaggerApiModelAnnotationFeature</values>
					<values>SwaggerApiModelPropertyAnnotationFeature</values>
					<values>IdField2StringFeature</values>
					<!-- 自定义增强特性 -->
					<values>com.github.hetianyi.learn.demo.config.Slf4jFeature</values>
				</features>
				<!-- 生成类名时忽略表的前缀 -->
				<ignorePrefixes>
					<value>t_</value>
					<value>tb_</value>
					<value>tab_</value>
					<value>_</value>
				</ignorePrefixes>
				<!-- 生成类名时忽略表的后缀 -->
				<ignoreSuffixes>
					<value>_</value>
				</ignoreSuffixes>
			</profile>
			<profile>
			...
			</profile>
			...
		</profiles>
	</configuration>
</plugin>
```



生成POJO代码

```shell
mvn code-generator:generate-pojo
```


### 内置增强特性列表

- ClassCommentFeature

  特性：生成类注释

- CopyrightFeature

  特性：生成版权注释，位于类第一行

- FieldCommentFeature

  特性：生成字段注释

- GetterSetterFeature

  特性：为类的字段添加Getters和Setters方法

- IdField2StringFeature

  特性：将数据库ID字段导出为String类型，而不是默认的Long类型

- ImportFeature

  特性：导入依赖

- ProjectBroadcastCommentFeature

  特性：生成本插件项目的传播注释

- DateFormatAnnotationFeature

  为时间日期格式的字段添加 @DateTimeFormat 注解

- JasonFormatAnnotationFeature

  特性：为时间日期格式的字段添加 @JsonFormat 注解

- IgnoreNullFieldAnnotationFeature

  为类添加 @JsonInclude(JsonInclude.Include.NON_NULL) 注解

- LombokAllArgsConstructorAnnotationFeature

  特性：为类添加 @AllArgsConstructor 注解

- LombokBuilderAnnotationFeature

  特性：为类添加 @Builder 注解

- LombokDataAnnotationFeature

  特性：为类添加 @Data 注解

- LombokNoArgsConstructorAnnotationFeature

  特性：为类添加 @NoArgsConstructor 注解

- MyBatisPlusClassAnnotationFeature

  特性：为类添加 Mybatisplus @com.baomidou.mybatisplus.annotation.TableName 注解

- MyBatisPlusFieldAnnotationFeature

  特性：为字段添加Mybatisplus字段的注解  
  包含 @com.baomidou.mybatisplus.annotation.TableId 
  和     @com.baomidou.mybatisplus.annotation.TableField

- ConvertibleFeature

  特性：为类添加 @Convertible 接口，方便进行类的转换和数据复制。

- SwaggerApiModelAnnotationFeature

  特性：为类添加Swagger的@ApiModel 注解

- SwaggerApiModelPropertyAnnotationFeature

  特性：为类的字段添加Swagger的@ApiModelProperty 注解

- JpaClassAnnotationFeature

  特性：为类添加Jpa的@Entity和@Table注解

- JpaFieldAnnotationFeature

  特性：为类的字段添加Jpa的@Column，@Id，@GeneratedValue，@GenerationType，@GenericGenerator注解

- SwaggerApiModelPropertyAnnotationFeature

  特性：为类的字段添加Swagger的@ApiModelProperty 注解

- SwaggerApiModelPropertyAnnotationFeature

  特性：为类的字段添加Swagger的@ApiModelProperty 注解



### 开发自定义特性

示例1：
```java
// 在类上添加 @Slf4j 注解
public class Slf4jFeature extends CustomFeature {

    public Slf4jFeature() {
        // 在标记点：CLASS_START的前面插入注解
		// 并添加依赖
        super(SlotType.CLASS_START, CustomFeature.InsertLocation.BEFORE, (s, g) -> {
            g.getImports().add("lombok.extern.slf4j.Slf4j");
            // 返回一个CodeSlot，插件自动将代码添加到指定位置
            return ImmutableList.of(CodeSlot.of("@Slf4j\n"));
        });
    }
}
```
示例2：
```java
// 移除id，created_time，updated_time三个字段
public class ExcludeIdAndTimeFieldFeature implements ExcludeField {
	@Override
	public Set<String> getExcludeFields(TableDefinition tabDef) {
		return ImmutableSet.of("id", "created_time", "updated_time");
	}

	@Override
	public void apply(ClassGenerator generator) {
	}
}
```

示例3：

```java
// 添加created_time_start和created_time_end字段
public class AddTimeRangeFieldFeature implements AddFieldFeature {
    @Override
    public List<TableColumn> getDateTimeRangeFields(TableDefinition tabDef) {
        List<TableColumn> result = new LinkedList<>();
        int pos = tabDef.getColumns().size();
        for (TableColumn column : tabDef.getColumns()) {
            if (column.getName().equalsIgnoreCase("created_time")) {
                result.add(TableColumn.builder()
                        .name("created_time_start")
                        .id(false)
                        .autoIncrement(false)
                        .comment("起始" + column.getComment())
                        .position(pos++)
                        .type(column.getType())
                        .build());
                result.add(TableColumn.builder()
                        .name("created_time_end")
                        .id(false)
                        .autoIncrement(false)
                        .comment("截止" + column.getComment())
                        .position(pos++)
                        .type(column.getType())
                        .build());
            }
        }
        return result;
    }

    @Override
    public void apply(ClassGenerator generator) {
    }
}
```

**标记点类型参考**：
```com.github.hetianyi.plugins.generator.pojo.generator.SlotType```



## 根据MVC模版代码生成增删改查代码



### 配置和使用

在```pom.xml```引入插件

```xml
<plugin>
	<groupId>com.github.hetianyi</groupId>
	<artifactId>code-generator-plugin</artifactId>
	<version>1.0.0-SNAPSHOT</version>
	<configuration>
		<!-- MySQL数据库配置信息 -->
		<database>
			<host>127.0.0.1</host>
			<port>3306</port>
			<dbName>test</dbName>
			<username>root</username>
			<password>123456</password>
		</database>
		<!-- 激活的profile -->
		<activeProfiles>
			<value>template</value>
		</activeProfiles>

		<!-- profile列表 -->
		<profiles>
			<profile>
				<!-- profile名称 -->
				<name>template</name>
				<!-- MVC模版代码根路径 -->
				<mvcTemplateDir>./template/mybatisplus</mvcTemplateDir>
				<!-- 只生成指定表的类，不指定默认生成所有表的类 -->
				<includeTables>
					<!--<value>t_user</value>-->
				</includeTables>
				<!-- 增强特性 -->
                <features>
                </features>
				<!-- 生成类名时忽略表的前缀 -->
				<ignorePrefixes>
					<value>t_</value>
					<value>_</value>
				</ignorePrefixes>
				<!-- 生成类名时忽略表的后缀 -->
				<ignoreSuffixes>
					<value>_</value>
				</ignoreSuffixes>
			</profile>
			<profile>
			...
			</profile>
			...
		</profiles>
	</configuration>
</plugin>
```

根据模版代码自动生成数据库表对应的MVC代码

```shell
mvn code-generator:generate-mvc
```

> 此功能需要自行修改提供的样例MVC模版代码，或者直接使用亦可，样例代码中的包名不能包含example字样。（替换程序硬编码包含example字样）



## 构建此插件

```shell
git clone https://github.com/hetianyi/code-generator-plugin.git
cd code-generator-plugin
mvn clean compile plugin:descriptor install
```



