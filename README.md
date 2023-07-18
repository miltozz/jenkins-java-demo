# Testing webhooks 
Trigger pipelines based on repo events(e.g push)

# Github

## Testing github webhooks for Jenkins - push trigger
- On push to the repo a Jenkins pipeline is triggered
- [Github Docs: Webhooks ](https://docs.github.com/en/webhooks-and-events/webhooks/about-webhooks-for-repositories)


## Jenkins - System - GitHub Server

### Plugin help notes
In this mode, Jenkins will add/remove hook URLs to GitHub based on the project configuration. Jenkins has a single post-commit hook URL for all the repositories, and this URL will be added to all the GitHub repositories Jenkins is interested in. You should provide credentials with scope admin:repo_hook for every repository which should be managed by Jenkins. It needs to read the current list of hooks, create new hooks and remove old hooks.

The Hook URL is http://13.38.217.197:8080/github-webhook/ , and it needs to be accessible from the internet. If you have a firewall and such between GitHub and Jenkins, you can set up a reverse proxy and override the hook URL that Jenkins registers to GitHub, by checking "override hook URL" in the advanced configuration and specify to which URL GitHub should POST.

### Steps
1. Manage Jenkins - System - Github Server
- API URL: `https://api.github.com`
- Credentials: 
    - Create personal access token in your account GitHub settings. Token should be registered with scopes:
        - admin:repo_hook - for managing hooks (read, write and delete old ones)
        - repo - to see private repos
        - repo:status - to manipulate commit statuses

    - In Jenkins, create credentials as «Secret Text», provided by Plain Credentials Plugin.
    - WARNING! Credentials are filtered on changing custom GitHub URL.

2. In pipeline settings(tested for simple pipeline, not multibranch)
- Check `GitHub project` and project url: `https://github.com/miltozz/jenkins-java-demo/`
- Check `GitHub hook trigger for GITScm polling`
- ..rest of pipeline settings...

3. Repository - Settings - Webhooks
- Payload URL: `http://[JENKINS-IP:PORT]/github-webhook/`
- Type: `application/json`
- Choose trigger events (default _push_)

4. Open firewall protocol and ports for Github hooks
- `https://api.github.com/meta`

---

# Gitlab

## Integration for Jenkins - push trigger
- On push to the repo a Jenkins pipeline is triggered
- [Gitlab Jenkins Integration](https://docs.github.com/en/webhooks-and-events/webhooks/about-webhooks-for-repositories)

## Info
- A Gitlab API token is needed on the project (Repo settings - Access tokens - scope:api) and then configured on the Jenkins Gitlab Plugin in Manage Jenkins - System - Gitlab Plugin
    - Enable authentication for ‘/project’ end-point.
    - Add, Jenkins Credential Provider
    - GitLab API token
    - paste the token value copied from GitLab
    - GitLab host URL: `https://gitlab.com/`
- Gitlab repo - Settings - Integrations - Jenkins
    - Active
    - Select Trigger
    - Example Jenkins URL: `http://11.11.11.111:8080`
    - Example URL for multibranch: `my-multibranch-pipeline/job/webhook-integration-simple-test/`
        - freestyle or pipeline is recommended, not multibranch
- Jenkins SG inbound: [GitLab.com](https://docs.gitlab.com/ee/user/gitlab_com/#ip-range) uses the IP ranges `34.74.90.64/28` and `34.74.226.0/24` for traffic from its Web/API fleet.
- Integration is Disabled