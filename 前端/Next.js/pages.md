pages/posts/[id].js===》posts/1、posts/2

## 预渲染

预渲染分为静态生成、服务端渲染



对于不涉及数据获取的页面，默认使用静态生成

#### 静态生成

对于涉及外部数据的预渲染：

1.内容取决于外部数据：使用 `getStaticProps`

2.path取决于外部数据：使用 `getStaticPaths` （通常还要同时使用 `getStaticProps`）

#### 服务端渲染

如果 page（页面）使用的是 **服务器端渲染**，则会在 **每次页面请求时** 重新生成页面的 HTML 。

要对 page（页面）使用服务器端渲染，你需要 `export` 一个名为 `getServerSideProps` 的 `async` 函数。服务器将在每次页面请求时调用此函数。

