<#import "common.ftl" as c>

<@c.page>
<div class="buttons">
    <form method="post" action="/loginVK">
        <button class="btn-hover vk">Login with VK</button>
    </form>
    <form method="post" action="/loginFB">
        <button class="btn-hover fb">Login with Facebook</button>
    </form>
</div>
</@c.page>
