<#-- @ftlvariable name="message" type="java.lang.String" -->
<#-- @ftlvariable name="example1" type="java.util.List<info.vadzimko.engine.Game.Attempt>" -->
<#-- @ftlvariable name="example2" type="java.util.List<info.vadzimko.engine.Game.Attempt>" -->

<#import "common.ftl" as c>

<@c.page>
    <div class="rules">
        <h1>Rules</h1>
        <p>
            &nbsp;&nbsp;&nbsp;&nbsp;The rules are pretty simple. Game lasts 1 minute and 30 seconds.
            The timer starts when the first player makes his number.
            Until the time is up, everyone who wants to take a part in the game should enter their number too.
            The winner of the game is the one who has guessed <em>unique</em> minimal positive number.
            Let's take a look at some examples:
        </p>
        <table class="table-fill">
            <thead>
            <tr>
                <th class="text-left">Name</th>
                <th class="text-left">Number</th>
            </tr>
            </thead>
            <tbody class="table-hover">
            <#list example1 as user>
            <tr>
                <td class="text-left">${user.getName()}</td>
                <td class="text-left">${user.getNumber()}</td>
            </tr>
            </#list>
            </tbody>
        </table>
        <p>
            &nbsp;&nbsp;&nbsp;&nbsp;Donald and Vladimir have chosen the minimal number and they both
            have lost because their numbers are the <em>same</em>. Next minimum belongs to Albert
            and he has won the game because his number is <em>unique</em>!
        </p>
        <table class="table-fill">
            <thead>
            <tr>
                <th class="text-left">Name</th>
                <th class="text-left">Number</th>
            </tr>
            </thead>
            <tbody class="table-hover">
            <#list example2 as user>
            <tr>
                <td class="text-left">${user.getName()}</td>
                <td class="text-left">${user.getNumber()}</td>
            </tr>
            </#list>
            </tbody>
        </table>
        <p>
            &nbsp;&nbsp;&nbsp;&nbsp;Tom has had the minimum number in this game
            and he has won because it's <em>unique</em>.
        </p>
        <p>
            &nbsp;&nbsp;&nbsp;&nbsp;As you can see, the game requires some skill from participants.
            You need to understand the probability of your number to win, depending on the number of participants,
            and you should be successful in predicting other players actions.
        </p>
        <hr>

        <h1>&nbsp;&nbsp;&nbsp;Invite your friends to see who's the best!</h1>
    </div>
</@c.page>
