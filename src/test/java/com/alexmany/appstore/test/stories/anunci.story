Meta: 
 
@theme parameters
 
Scenario: scenari crea foto abans de crear anunci
 
Given I save a foto without presaved anunci
When I get <response>
Then If I create anunci has the same <id>

Examples:
response|id
{"ok":"ok","id":"1"}|1

Scenario: crear anunci i despres la foto

Given I create anunci
When I get <response>
Then If I save a foto has the same <id>

Examples:
response|id
{"ok":"ok","id":"1"}|1
