## Visitor Pattern (访问者模式)
这是一个访问者模式的测试

测试的结构是一个类似于多叉树的结构

                      root
        rootChild1   rootChild2  rootChild3
child11  child12      child21     child31   child32

visitor 模式主要包含如下几个类型:

1. Visitor
对访问节点进行操作
Visitor包含一个函数`visit`接收一个Element对象,在该函数内部是操作element的逻辑

2. Element
构成结构的元素, visitor需要操作的就是该类型
Element 包含一个函数`accept`接收一个visitor，在该函数内部调用visitor操作节点的函数

3. Application
使用visitor操作Element
根据Element形成的结构，进行遍历，并对每一个元素调用`accept`

