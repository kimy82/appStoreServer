Meta: 
 
@theme parameters
 
Scenario: scenario delte then not exist
 
Given I delete a <email>
When I recieve <response>
Then It do not exist in BBDD and the response is <responselogin>

Examples:
email|response|responselogin
joaquim.orra@gmail.com|{"ok":"ok"}|{"ok":"ko"}
joaquim.orra@gmail|{"ok":"ok"}|{"ok":"ko"}
joaqñim.òrrá@gmail.es|{"ok":"ok"}|{"ok":"ko"}
joaqñim.òrrá@gmail.es|{"ok":"ok"}|{"ok":"ko"}
xxxx|{"ok":"ko"}|{"ok":"ko"}
null|{"ok":"ko"}|{"ok":"ko"}

Scenario: insert then exist

Given I insert a <user> with <email>
When I recieve <response>
Then I can login with response <responselogin>

Examples:
user|email|response|responselogin
kim|joaquim.orra@gmail.com|{"ok":"ok","id":"1"}|{"ok":"ok","username":"kim","role":"ROLE_CLIENT","id":"1"}
kim|joaquim.orra@gmail|{"ok":"ok","id":"1"}|{"ok":"ok","username":"kim","role":"ROLE_CLIENT","id":"1"}
kim|xxxx|{"ok":"ko"}|{"ok":"ok","username":"kim","role":"ROLE_CLIENT","id":"1"}
kim|null|{"ok":"ko"}|{"ok":"ko"}