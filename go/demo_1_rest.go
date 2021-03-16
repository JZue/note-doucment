package main

import (
	"github.com/julienschmidt/httprouter"
	"net/http"
	"fmt"
)
//解析请求中带的参数
func getuser (w http.ResponseWriter, r *http.Request, params httprouter.Params) {

	uid := params.ByName("uuid")
	fmt.Fprintf(w,"获取到用户ID:%s\n",uid)

	//获取请求方式
	mo := r.Method
	fmt.Fprintf(w,"获取到请求方式为:%s",mo)
}

//go使用net/http实现restful
func main() {

	//1.调用 httprouter 生成句柄
	router := httprouter.New()

	//2.通过句柄进行路由解析
	router.GET("/getuser/:uuid",getuser)

	//3.将句柄放入http.handle
	http.Handle("/",router)

	//4.建立网络,等待网络连接
	http.ListenAndServe(":10010",nil)

}

