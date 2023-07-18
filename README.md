# Github

## Testing github webhooks for Jenkins - push trigger
- On push to the repo a Jenkins pipeline is triggered
- [Github Docs: Webhooks ](https://docs.github.com/en/webhooks-and-events/webhooks/about-webhooks-for-repositories)


















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