Citrix Workspace Microapps
==========================

Catalog of Microapp bundles for Citrix Workspace.


Repositories configuration
--------------------------

### Pushing to the destination repository from GitHub action

Generate a new pair of SSH keys.

    ssh-keygen

Add private key to the source repository that executes GitHub action, name it `DEST_REPO_SSH_KEY`.

https://github.com/michaltc/workspace-microapps/settings/secrets/

Add public key to the destination repository and enable `Allow write access` option.

https://github.com/michaltc/workspace-microapps-bundles/settings/keys/


### Delete branch after merge

Enable `Automatically delete head branches` in settings.

https://github.com/michaltc/workspace-microapps/settings
