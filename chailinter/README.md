# ChaiLinter Library

An issue is a potential bug that can be created by a contributor as they implement a design in in the app. 
- To discover an issue we use [detectors] that have associated severities - called [Severity] in the detector.

**_`Issues`_** and **_`detectors` are separate classes because a **_`detector`_** can discover multiple different issues
as it's analyzing code, and we want to be able to different severities for different issues, 
the ability to suppress one but not other issues from the same detector, and so on.

A `_**detector**_` is able to find a particular problem (or a set of related problems). 
Each problem type is uniquely identified as an Issue.

These are arranged as:
- [ChaiLinterIssueRegistry] is the central location where the possible Chai design system violations are listed.

For a cleaner approach, specific violations are listed in the [detector] package:
- [ChaiIncorrectUsageDetector] -  Detects whether there are any violations from using chai design system.
- [IncorrectColourUsageDetector] -  Detects whether colours in chai have been used correctly.
- 