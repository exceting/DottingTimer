<div class="tree_well">
	<div class="unit_info">
		<div class="unit_avater" style="border: 4px solid #ff84a1; background-image: url('/images/avater.png');"></div>
		<div class="unit_intro">
			图例：<span class="label label-success">正常</span>
			<span class="label label-warning">超出预期</span>
			<span class="label label-danger">异常</span>
			&nbsp;&nbsp;&nbsp;&nbsp;
			ps：图中带有淡背景色的为合并节点将会被节点，由于内部循环操作导致的同一父节点下存在相同合并，点击节点可查看详细信息
		</div>
	</div>

	<div style="padding-top: 80px; text-align: center"><h3>链 路 详 情</h3></div>
	<hr/>
	<div class="unit_info">
		<div class="unit_avater" style="border: 4px solid #519dff; background-image: url('/images/avater2.png');"></div>
		<div class="unit_intro_main">
			下面是该链路主线程发生的调用，看图时可以参照上述图例解释，<span style="color: red">红色节点</span>代表出错
		</div>
	</div>
<div class="master">

</div>
</div>

<div class="tree_well">

	<div class="unit_info">
		<div class="unit_avater" style="border: 4px solid #519dff; background-image: url('/images/avater2.png');"></div>
		<div class="unit_intro_main">
			下面是该链路主线程产生的异步处理子线程链路，<span class="async_num"></span>
		</div>
	</div>
<div class="slave">

</div>
</div>