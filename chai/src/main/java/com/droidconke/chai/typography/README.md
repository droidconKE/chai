# Typography
These are the typography tokens in the design system.

## Titles
For titles we have 2 tokens:
- title
- header

How should I use them?

### Title
This is the **ONLY** token to use for the main purpose of a screen, and is the topmost hierarchy in any screen.

> It should only be used or appear once in a screen

Example: 
If you have an app with two screens, characters and character, where characters shows a list of people and character shows the details of one person.
You can use title for Characters and Character .

```kotlin

    ChaiTextTitle(text = "Your Title Goes here")

```

### Header
This is the token to use for sections inside a screen of different purposes.
- it can appear more than once
- it should be used to show different sections of the app
- it should NOT be used as a screen title, for a screen title use [this](#title)

Example: 
```kotlin

    ChaiTextHeader(text = "Your Header Goes here")

```

## Content
For content items in the app we have 3 tokens:
- display
- paragraph
- body
- label

Usage

### Display
This is the token to use when you want to highlight or make any content stand out. It should not be used as a header, title or body text.
Example:
```kotlin

    ChaiTextDisplay(text = "Your text goes here")

```

### Paragraph
This is the token to use with [Header](#header) to write a brief description of the header
Example:
```kotlin

    ChaiTextParagraph(text = "Your text goes here")

```

### Body
This is the token to use for the main content of any screen.
Example:
```kotlin

    ChaiTextBody(text = "Your text goes here")

```

### Label
This is the token to use for any labelling in a screen or component
Example:
```kotlin

    ChaiTextLabel(text = "Your text goes here")

```
