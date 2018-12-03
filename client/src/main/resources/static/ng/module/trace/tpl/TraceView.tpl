<form class="form-horizontal">
	<div class="form-group">

        <label class="col-md-2 control-label">trace id</label>
		<div class="col-md-2">
			<input type="text" class="j_s_trace_id form-control">
		</div>

		<div class="col-md-2">
			<button class="j_search btn btn-default" type="button">检索</button>
		</div>
	</div>
</form>

<table class="table table-striped table-condensed admin">
<thead>
<tr>
    <th style="width: 10rem">trace ID</th>
    <th style="width: 10rem">span ID</th>
    <th style="width: 10rem">总耗时（ms）</th>
    <th style="width: 10rem">是否异常</th>
    <th style="width: 10rem">是否包含异步</th>
    <th style="width: 10rem">预期（ms）</th>
    <th style="width: 15rem">项目名</th>
    <th style="width: 30rem">方法名</th>
    <th style="width: 10rem">触发时间</th>
    <th style="width: 10rem">操作</th>
</tr>
</thead>
<tbody></tbody>
</table>
<nav id="pageNav" class="center"></nav>
<script id="trTemplate" type="text/template">
<tr>
    <td class="j_trace_id"></td>
    <td class="j_span_id"></td>
    <td class="j_time"></td>
    <td class="j_is_ex"></td>
    <td class="j_is_async"></td>
    <td class="j_expect"></td>
    <td class="j_moudle"></td>
    <td class="j_title"></td>
    <td class="j_ctime"></td>
    <td class="j_button"></td>
</tr>
</script>