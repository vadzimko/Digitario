<#-- @ftlvariable name="Name" type="java.lang.String" -->
<#-- @ftlvariable name="timer" type="java.lang.Integer" -->
<#-- @ftlvariable name="started" type="java.lang.Boolean" -->
<#-- @ftlvariable name="participating" type="java.lang.Boolean" -->
<#-- @ftlvariable name="number" type="java.lang.Integer" -->
<#-- @ftlvariable name="participants" type="java.lang.Integer" -->
<#-- @ftlvariable name="lastWinner" type="java.lang.String" -->

<#import "common.ftl" as c>

<@c.page>
    <div class="nested" >
    <div class="game_info">

    <#if started == true>
    <ul class="nav timer">
        <li>
            <p id="timer">Timer: 00:00:00</p>
                <script type="text/javascript">
                    TIMER(${timer});
                </script>
        </li>
        <li><p>Participants: ${participants}</p></li>
    </ul>
    <#else>
        <p>Be the one who will start a game</p>
    </#if>

    <#if participating!false == true>
        <h3>You have submitted number: ${number}</h3>
    <#else>

    <form class="number_form" action="/game" method="post" autocomplete="off">
        <h1>Guess a number</h1>
        <div class="question">
            <input name="answer" type="number" min="1" required/>
            <input name="action" type="hidden" value="addAnswer"/>
        </div>
        <button>Submit</button>
    </form>
    </#if>
    </div>
    <footer class="footer">
        <hr>
        <ul class="nav">
            <li><p class="last_winner">Last winner: ${lastWinner!"vadzimko"}</p></li>
            <li><p class="results"><a href="/LastGame">Results</a></p></li>
            <li><p><a href="/rules">Rules</a></p></li>
            <li>
                <p class="logged">
                    Logged in as ${Name}
                    |
                    <a href="/logout">Logout</a>
                </p>
            </li>
        </ul>
    </footer>
    </div>
</@c.page>
