== User
+ String userId
+ PublicUserInfo publicUserInfo

== PublicUserInfo
+ Image avatar
+ Properties contactDetails
+ List<Group> groups
+ List<UserItem> items

== UserItem
+ String type # enum?
+ String value
+ Image image

== Group
+ Strina name
+ String description

== MapItem
+ String type # should it be enum?
+ Point position
+ Image image
+ Properties properties
+ Date lastModification #? 

== LayerDiff
+ List<Item> items
+ String name
+ String description

== UserState
+ Point position
+ Double speed
+ # any others?

