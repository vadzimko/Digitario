<#-- @ftlvariable name="users" type="java.util.List<info.vadzimko.engine.Game.Attempt>" -->
<#import "common.ftl" as c>

<@c.page>
<div class="table-title">
    <h3>Results</h3>
</div>
<table class="table-fill">
<thead>
<tr>
    <th class="text-left">Name</th>
    <th class="text-left">Number</th>
</tr>
</thead>
<tbody class="table-hover">
<#list users as user>
    <tr>
        <td class="text-left">${user.getName()}</td>
        <td class="text-left">${user.getNumber()}</td>
    </tr>
</#list>
</tbody>
</table>
</@c.page>
