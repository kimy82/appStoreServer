Meta: 
 
@theme parameters
 
Scenario: I get all anuncis

Given There are <x> anuncis
When I the anuncis with <init>
Then I get <y> anuncis

Examples:
x |init|y
10|0   |10
30|0   |20
10|20  |0
