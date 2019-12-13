Citrix Workspace Microapps
==========================

*Catalog of [Microapp][microapps] bundles for [Citrix Workspace][workspace].*

- Source repository: https://github.com/michaltc/workspace-microapps/
- Generated destination repository: https://github.com/michaltc/workspace-microapps-bundles/

![](https://github.com/michaltc/workspace-microapps/workflows/Prepare%20bundles/badge.svg)
![](https://github.com/michaltc/workspace-microapps/workflows/Delete%20obsolete%20branches/badge.svg)


High level design
-----------------

The source repository consists of three main directories.

- `bundles`: The bundles catalog, metadata, data files ~ source of truth.
- `bundlegen`: A tool that validates the source bundles catalog and transforms it to the destination form.
- `.github`: [GitHub Actions][github-actions] (CI/CD pipeline), executes bundlegen and commits generated files
to the destination repository.

The destination repository contains only one directory.

- `bundles`: Generated files of the bundles catalog.

The GitHub action is triggered on each push to the source source repository. It generates the output files, commits
and pushes them to the destination repository.

Branching and feature branches are fully supported, branch in the destination repository uses the same name as the
branch in the source one. If a branch is removed from the source repository (after merge or manually) it will be removed
also from the destination one.

Bundlegen is tightly coupled with the bundles catalog. It is built as part of the CI/CD pipeline so all structural
changes in the bundles catalog can be implemented in a feature branch and then atomically merged to the master branch,
preferably after rebase.


Configuration, deployment
-------------------------

### Links to GitHub repositories

Edit config/env variables at the top of `.github/workflows/*.yml` GitHub actions.


### Pushing to the destination repository from GitHub actions

Generate a new pair of SSH keys.

    ssh-keygen

https://github.com/michaltc/workspace-microapps/settings/secrets/

- Add **private key** to the **source repository** that executes GitHub action, name it `DEST_REPO_SSH_KEY`.

https://github.com/michaltc/workspace-microapps-bundles/settings/keys/

- Add **public key** to the **destination repository** and enable `Allow write access` option.


### Protect `master` branch

https://github.com/michaltc/workspace-microapps/settings/branch_protection_rules/new

- Insert `master` to `Branch name pattern`.
- Enable `Require pull request reviews before merging`.
    - Enable `Dismiss stale pull request approvals when new commits are pushed`.
- Enable `Require status checks to pass before merging`.
    - Enable `build`.
- Consider to enable `Require linear history`.
- Consider to enable `Include administrators`.
- Disable `Allow force pushes`.
- Disable `Allow deletions`.


Development
-----------

Bundlegen can be executed locally, it operates on filesystem level with no references to GitHub or any other
cloud service (at the moment of writing).

- Java/Maven project, JUnit unit tests.
- Main class: `com.citrix.microapps.bundlegen.BundlegenMain`.
- The tool shows usage when no arguments are passed.

Changes should be always implemented in a feature branch and properly tested. **Anything that appears in `master` branch
may be quickly visible by production system of all the customers.** Fail fast in the code to break CI/CD pipeline and
to skip updating of the destination repository.

**Bundlegen tries hard to minimize number of changes committed to the destination repository to limit its uncontrolled
grow as much as possible.** Filesystem entries should be iterated always in the same order, zip archives should differ
(on byte level) only when a source file changed. No timestamp based on current time should appear in any generated file.


[workspace]: https://www.citrix.com/products/citrix-workspace/
[microapps]: https://www.citrix.com/digital-workspace/microapps.html
[github-actions]: https://help.github.com/en/actions/automating-your-workflow-with-github-actions
