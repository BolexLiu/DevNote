#前言
本篇题目太大,所以打算分为三篇来讲解.写下此篇文章更多的是对知识的一个回顾与分享.有一部分内容转载GItHub上的,链接在末尾.还请各路大神多多指教.
- [快抱紧我,教你撸出一个简单的React-Naitive [入门基础篇]]()
- [快抱紧我,教你撸出一个简单的React-Naitive [应用搭建篇]]()
- [快抱紧我,教你撸出一个简单的React-Naitive [打包发布篇]]()

这里没有讲到如何搭建Node&android环境等,不过你可以参考官方此篇文章.相信你一步步来,
一定可以搭建好的.
http://reactnative.cn/docs/0.39/getting-started.html

# 本篇适合那些人看?
- 拥有前端经验
- 从android|IOS转来RN玩玩
- 有ES6基础(没有也没关系)
- 掌握基本的JS
- 只要你想学还有什么能阻止你的激情?

---

# 通过本篇你能学到什么?
- ES6常用基础语法
- React-Naitive组件基础
---

# ES6是什么鬼?
  ECMAScript 6.0（以下简称ES6）是JavaScript语言的下一代标准，已经在2015年6月正式发布了。它的目标，是使得JavaScript语言可以用来编写复杂的大型应用程序，成为企业级开发语言。 --[《CMAScript 6 入门》](http://es6.ruanyifeng.com/)强烈推荐想系统学习的同学参考该站.http://es6.ruanyifeng.com/
#### 说好的常用基础语法呢?
   实际ES6的东西可能太多,可能会成为我们学习React-Native上的一个绊脚石.其实也可以先学一些常用的基础语法.等知识不足以完成我们的项目的时候回过头来翻文档看.也许这样才更适合我们成长.但文档我们有时间的时候还是一定要细看.毕竟这是你升职、加薪、装逼必备的技能呀.   好了废话不说辣么多啦.开整!搞事!

![](http://upload-images.jianshu.io/upload_images/1110736-3d2ad05a45629d34.png?imageMogr2/auto-orient/strip%7CimageView2/2/h/150)

### 变量声明

#### const 和 let

不要用 `var`，而是用 `const` 和 `let`，分别表示常量和变量。不同于 `var` 的函数作用域，`const` 和 `let` 都是块级作用域。

```javascript
const DELAY = 1000;

let count = 0;
count = count + 1;
```

#### 模板字符串

模板字符串提供了另一种做字符串组合的方法。

```javascript
const user = 'world';
console.log(`hello ${user}`);  // hello world

// 多行
const content = `
  Hello ${firstName},
  Thanks for ordering ${qty} tickets to ${event}.
`;
```

#### 默认参数

```javascript
function logActivity(activity = 'skiing') {
  console.log(activity);
}

logActivity();  // skiing
```

### 箭头函数

函数的快捷写法，不需要通过 `function` 关键字创建函数，并且还可以省略 `return` 关键字。

同时，箭头函数还会继承当前上下文的 `this` 关键字。

比如：

```javascript
[1, 2, 3].map(x => x + 1);  // [2, 3, 4]
```

等同于：

```javascript
[1, 2, 3].map((function(x) {
  return x + 1;
}).bind(this));
```

### 模块的 Import 和 Export

`import` 用于引入模块，`export` 用于导出模块。

比如：

```javascript
// 引入全部
import dva from 'dva';

// 引入部分
import { connect } from 'dva';
import { Link, Route } from 'dva/router';

// 引入全部并作为 github 对象
import * as github from './services/github';

// 导出默认
export default App;
// 部分导出，需 import { App } from './file'; 引入
export class App extend Component {};
```

### ES6 对象和数组

#### 析构赋值

析构赋值让我们从 Object 或 Array 里取部分数据存为变量。

```javascript
// 对象
const user = { name: 'guanguan', age: 2 };
const { name, age } = user;
console.log(`${name} : ${age}`);  // guanguan : 2

// 数组
const arr = [1, 2];
const [foo, bar] = arr;
console.log(foo);  // 1
```

我们也可以析构传入的函数参数。

```javascript
const add = (state, { payload }) => {
  return state.concat(payload);
};
```

析构时还可以配 alias，让代码更具有语义。

```javascript
const add = (state, { payload: todo }) => {
  return state.concat(todo);
};
```
# RN是什么?
React Native (简称RN)是Facebook于2015年4月开源的跨平台移动应用开发框架，是Facebook早先开源的UI框架 React 在原生移动应用平台的衍生产物，目前支持iOS和安卓两大平台。RN使用Javascript语言，类似于HTML的JSX，以及CSS来开发移动应用，因此熟悉Web前端开发的技术人员只需很少的学习就可以进入移动应用开发领域。

务必按照官网来入门!
务必按照官网来入门!
务必按照官网来入门!

重要的事情说三遍.[RN中文官网](http://reactnative.cn/) http://reactnative.cn/
到目前笔者写到这里RN在2017年1月5号已经上线0.40,当前版本官方的中文文档还没全面.如果你和我一样.英语水平并没有那么腻害.0.39的的文档也可以看.但要注意0.40中可能发生了某些改变的新特性.可以参考使用.

## React Component

###  Stateless Functional Components

React Component 有 3 种定义方式，分别是 `React.createClass`, `class` 和 `Stateless Functional Component`。推荐尽量使用最后一种，保持简洁和无状态。这是函数，不是 Object，没有 `this` 作用域，是 pure function。

比如定义 App Component 。

```javascript
function App(props) {
  function handleClick() {
    props.dispatch({ type: 'app/create' });
  }
  return <div onClick={handleClick}>${props.name}</div>
}
```

等同于：

```javascript
class App extends React.Componnet {
  handleClick() {
    this.props.dispatch({ type: 'app/create' });
  }
  render() {
    return <div onClick={this.handleClick.bind(this)}>${this.props.name}</div>
  }
}
```

### JSX

#### Component 嵌套

类似 HTML，JSX 里可以给组件添加子组件。

```html
<App>
  <Header />
  <MainContent />
  <Footer />
</App>
```

#### className

`class` 是保留词，所以添加样式时，需用 `className` 代替 `class` 。

```html
<h1 className="fancy">Hello dva</h1>
```

#### JavaScript 表达式

JavaScript 表达式需要用 `{}` 括起来，会执行并返回结果。

比如：

```javascript
<h1>{ this.props.title }</h1>
```

#### Mapping Arrays to JSX

可以把数组映射为 JSX 元素列表。

```javascript
<ul>
  { this.props.todos.map((todo, i) => <li key={i}>{todo}</li>) }
</ul>
```





#### Spread Attributes

这是 JSX 从 ECMAScript6 借鉴过来的很有用的特性，用于扩充组件 props 。

比如：

```javascript
const attrs = {
  href: 'http://example.org',
  target: '_blank',
};
<a {...attrs}>Hello</a>
```

等同于

```javascript
const attrs = {
  href: 'http://example.org',
  target: '_blank',
};
<a href={attrs.href} target={attrs.target}>Hello</a>
```

### Props

数据处理在 React 中是非常重要的概念之一，分别可以通过 props, state 和 context 来处理数据。而在 dva 应用里，你只需关心 props 。

#### propTypes

JavaScript 是弱类型语言，所以请尽量声明 propTypes 对 props 进行校验，以减少不必要的问题。

```javascript
function App(props) {
  return <div>{props.name}</div>;
}
App.propTypes = {
  name: React.PropTypes.string.isRequired,
};
```

内置的 prop type 有：

- PropTypes.array
- PropTypes.bool
- PropTypes.func
- PropTypes.number
- PropTypes.object
- PropTypes.string

#### 往下传数据

![](https://zos.alipayobjects.com/rmsportal/NAzeMyUoPMqxfRv.png)

#### 往上传数据

![](https://zos.alipayobjects.com/rmsportal/fiKKgDGuEJfSvxv.png)

### CSS Modules

![](https://zos.alipayobjects.com/rmsportal/mHVRpjNYhVuFdsS.png)

#### 理解 CSS Modules

一张图理解 CSS Modules 的工作原理：

![](https://zos.alipayobjects.com/rmsportal/SWBwWTbZKqxwEPq.png)

`button` class 在构建之后会被重命名为 `ProductList_button_1FU0u` 。`button` 是 local name，而 `ProductList_button_1FU0u` 是 global name 。**你可以用简短的描述性名字，而不需要关心命名冲突问题。**

然后你要做的全部事情就是在 css/less 文件里写 `.button {...}`，并在组件里通过 `styles.button` 来引用他。

#### 定义全局 CSS

CSS Modules 默认是局部作用域的，想要声明一个全局规则，可用 `:global` 语法。

比如：

```css
.title {
  color: red;
}
:global(.title) {
  color: green;
}
```

然后在引用的时候：

```javascript
<App className={styles.title} /> // red
<App className="title" />        // green
```

#### `classnames` Package

在一些复杂的场景中，一个元素可能对应多个 className，而每个 className 又基于一些条件来决定是否出现。这时，[classnames](https://github.com/JedWatson/classnames) 这个库就非常有用。

```javascript
import classnames from 'classnames';
const App = (props) => {
  const cls = classnames({
    btn: true,
    btnLarge: props.type === 'submit',
    btnSmall: props.type === 'edit',
  });
  return <div className={ cls } />;
}
```

这样，传入不同的 type 给 App 组件，就会返回不同的 className 组合：

```javascript
<App type="submit" /> // btn btnLarge
<App type="edit" />   // btn btnSmall
```
---
##本文引用内容:
- **[dva-knowledgemap](https://github.com/dvajs/dva-knowledgemap)**
注:没有任何人可以不通过实践和努力学习到精华,更不可能看完本篇就完全掌握住.React-Native(以下简称NR)是一个react的子集.然而react生态圈异常庞大.作者也是今年才开始学习这门技术的.如果想运用到正式开发中.还需要自己学习更多,去探索.
