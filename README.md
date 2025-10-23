
[![Chai CI 🍵](https://github.com/droidconKE/chai/actions/workflows/main.yml/badge.svg)](https://github.com/droidconKE/chai/actions/workflows/main.yml)

<p align="center">
  <a href="https://github.com/droidconKE/droidconKE2022Android">
    <img src="https://raw.githubusercontent.com/droidconKE/iconPack/master/images/chaicover.png" alt="Chai Design Logo" width=330 height=150>
  </a>
</p>

## Chai Design System
---

What is a design system:

To learn more about this look at this [Why Design Systems](https://github.com/droidconKE/chai/blob/master/docs/whyDesignSystems.md) -  Explains the need for a design system in the context of compose.

## About Chai

Chai is swahili word for tea.
This is the droidconKE design system.

Here are the design docs links :
- [Design System](https://xd.adobe.com/view/8fafeec6-a92d-48dd-bab9-f3e46775257a-dafc/)
- [Screens](https://xd.adobe.com/view/eb1ed4ed-fd4d-4ba2-b2f7-a91c7379a022-be4d/screen/cfea72b5-9007-4335-ae86-9162594c094f/)

This Project shows how you can use this design system in a multi module app monoRepo set up.

### Structure Of Chai's Design System Project,

The Chai Design System Project Architecture is captured in detail:
1. [Why Design Systems](https://github.com/droidconKE/chai/blob/master/docs/whyDesignSystems.md) -  Explains the need for a design system in the context of compose.
2. [Architecture](https://github.com/droidconKE/chai/blob/master/docs/architecture.md) -  The architecture of the project
3. [Chai Design System Architecture](https://github.com/droidconKE/chai/blob/master/docs/chaiArchitecture.md) -  The architecture of the design system
4. [buildlogic](https://github.com/droidconKE/chai/blob/master/docs/buildlogic.md) -  Handles how we build the app wiyth gradle, ditches the build src in favour of convengion polugins.
5. [chaiLinter](https://github.com/droidconKE/chai/blob/master/docs/chaiLinter.md) -  Explains the design system linter
6. [Atoms](https://github.com/droidconKE/chai/blob/master/docs/atoms.md) - Explains the atoms in the design system
7. [Components](https://github.com/droidconKE/chai/blob/master/docs/components.md) -  Design system components
8. [chaidemop](https://github.com/droidconKE/chai/blob/master/docs/components.md) -  Design system components

## Implementing Chai

To Implement chai,

See the example implementation that  exists by running [chaidemo] that contains the various implementations of the elements of the design system.

### [Running Project]
Known issue with gradle:
- If you run into an error when building project, ist probably a false negative.
- Run(Or just press green play icon on android studio from the left here:
 `./gradlew sync` and output complete html report should not display errors then
  `./gradlew tasks` to see a list of tasks you can run from the root of the project 

or just press the gradle icon with the downward arrow at the top right of android studio to sync project with gradle files and you should be ok.



### [Tasks 🚧]
- [ ] Documentation
  - [ ] Architecture
  - [ ] Reason for Design System
  - [ ] Constituents of Chai
- [ ] Build System
- [ ] Project Infrastructure setUp
  - [ ] Workflows
    - [ ] Git Workflows
    - [ ] Release Pipelines
    - [ ] Publishing
      - [ ] Sample App release to playstore: chaidemo
      - [ ] publish to maven
- [ ] Build System
  - [ ] Build Logic setUP
  - [ ] Convention Plugins setUp
- [ ] Sample Application
- [ ] Testing
    - [ ] Add Compose UI tests for design system components (instrumented tests)
        - [ ] Test component rendering (CPrimaryButton, ChaiText, etc.)
        - [ ] Test theme switching behavior (light/dark mode)
        - [ ] Test accessibility semantics
        - [ ] Add screenshot/visual regression tests
    - [ ] Add E2E tests example for chaidemo app
        - [ ] Test complete user flow: Navigate through all demo screens
        - [ ] Test component interactions in real app context
        - [ ] Verify all design system components display correctly in demo

## Contributing
 [Hop on here for a chat and ask questions](https://github.com/droidconKE/chai/discussions). NO DMs please :)
