Meta: 
 
@theme parameters
 
Scenario: scenario delte then not exist
 
Given I delete a <user>
When I recieve <response>
Then It do not exist in BBDD and the response is <responselogin>

Examples:
user|response|responselogin
joaquim.orra@gmail.com|{"ok":"ok"}|{"ok":"ko"}
joaquim.orra@gmail|{"ok":"ok"}|{"ok":"ko"}
joaqñim.òrrá@gmail.es|{"ok":"ok"}|{"ok":"ko"}
joaqñim.òrrá@gmail.es|{"ok":"ok"}|{"ok":"ko"}
xxxx|{"ok":"ko"}|{"ok":"ko"}
null|{"ok":"ko"}|{"ok":"ko"}

Scenario: insert then exist

Given I insert a <user>
When I recieve <response>
Then I can login with response <responselogin>

Examples:
user|response|responselogin
joaquim.orra@gmail.com|{"ok":"ok"}|{"ok":"ok","username":"joaquim.orra@gmail.com","role":"ROLE_CLIENT"}
joaquim.orra@gmail|{"ok":"ok"}|{"ok":"ok","username":"joaquim.orra@gmail","role":"ROLE_CLIENT"}
xxxx|{"ok":"ko"}|{"ok":"ok","username":"xxxx","role":"ROLE_CLIENT"}
null|{"ok":"ko"}|{"ok":"ko"}