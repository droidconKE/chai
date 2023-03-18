# A note on RenovateBot

`"baseBranches"` are just the branches that renovate bot will raise PRs against.Instead of 
separate branches it will raise it to just this branch specifically 'dependencyUpdates'.

By default renovate bot will group all updates together `"group:all"`.

What ` "groupName": "kotlin"` does is group the 
Kotlin compose and ksp updates together. This groups them together as `kotlin` updates that will be separate from 
the other changes.

If we have other libraries that are known to wokr together we can always come back and group them 
together with other package rules separate from all
