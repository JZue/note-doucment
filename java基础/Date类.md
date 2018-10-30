#时间类Date详解
###标准Date

	Date类本身的数据格式是如下的：
		Thu Jul 06 13:39:12 CST 2017
	也就是说date类的实例的格式一定是这样的，
	如果想获取其他格式的时间类型，可以用SimpleDateFormat的实例，但是他们返回的非默认格式的时间的数据类型都是字符串的，
###@DateTimeFormat注解 
	实际上返回的是一个string类型的规定的时间格式的时间