package com.bond.testgithub;

import com.bond.testgithub.objs.RecyclerDataItem;
import com.bond.testgithub.ui.main.UserSettings;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TestGithubJSONparse {
  @Test
  public void parse_isCorrect() {
    RecyclerDataItem github_repo =
        UserSettings.jsonToRecyclerDataItem(githubJsonExample1);
    boolean res = null != github_repo
        && github_repo.str1 != null
        && github_repo.str2 != null
        && github_repo.json != null
        && github_repo.img_url != null
        && github_repo.jsonParsed != null
        && github_repo.jsonParsed.get() != null;
    assertEquals(res, true);
    res = owner1.equals(github_repo.str1)
        && repo1.equals(github_repo.str2)
        && url1.equals(github_repo.img_url)
        && githubJsonExample1.equals(github_repo.json);
    assertEquals(res, true);

    github_repo =
        UserSettings.jsonToRecyclerDataItem(githubJsonExample2);
     res = null != github_repo
        && github_repo.str1 != null
        && github_repo.str2 != null
        && github_repo.json != null
        && github_repo.img_url != null
        && github_repo.jsonParsed != null
        && github_repo.jsonParsed.get() != null;
    assertEquals(res, true);
    res = owner2.equals(github_repo.str1)
        && repo2.equals(github_repo.str2)
        && url2.equals(github_repo.img_url)
        && githubJsonExample2.equals(github_repo.json);
    assertEquals(res, true);
  }

  final static String owner1 = "test_login";
  final static String repo1 = "test_repo";
  final static String url1 = "https://avatars2.githubusercontent.com/u/4355665?v=4";

  final static String githubJsonExample1 = "{\"name\": \"test_repo\",\"owner\": {\"login\": \"test_login\",\"avatar_url\": \"https://avatars2.githubusercontent.com/u/4355665?v=4\"},\"url\": \"https://api.github.com/repos/WheezePuppet/specnet\"}";

  final static String owner2 = "WheezePuppet";
  final static String repo2 = "specnet";
  final static String url2 = "https://avatars2.githubusercontent.com/u/4355665?v=4";
  final static String githubJsonExample2 = "{\n" +
      "  \"id\": 170547613,\n" +
      "  \"node_id\": \"MDEwOlJlcG9zaXRvcnkxNzA1NDc2MTM=\",\n" +
      "  \"name\": \"specnet\",\n" +
      "  \"full_name\": \"WheezePuppet/specnet\",\n" +
      "  \"private\": false,\n" +
      "  \"owner\": {\n" +
      "    \"login\": \"WheezePuppet\",\n" +
      "    \"id\": 4355665,\n" +
      "    \"node_id\": \"MDQ6VXNlcjQzNTU2NjU=\",\n" +
      "    \"avatar_url\": \"https://avatars2.githubusercontent.com/u/4355665?v=4\",\n" +
      "    \"gravatar_id\": \"\",\n" +
      "    \"url\": \"https://api.github.com/users/WheezePuppet\",\n" +
      "    \"html_url\": \"https://github.com/WheezePuppet\",\n" +
      "    \"followers_url\": \"https://api.github.com/users/WheezePuppet/followers\",\n" +
      "    \"following_url\": \"https://api.github.com/users/WheezePuppet/following{/other_user}\",\n" +
      "    \"gists_url\": \"https://api.github.com/users/WheezePuppet/gists{/gist_id}\",\n" +
      "    \"starred_url\": \"https://api.github.com/users/WheezePuppet/starred{/owner}{/repo}\",\n" +
      "    \"subscriptions_url\": \"https://api.github.com/users/WheezePuppet/subscriptions\",\n" +
      "    \"organizations_url\": \"https://api.github.com/users/WheezePuppet/orgs\",\n" +
      "    \"repos_url\": \"https://api.github.com/users/WheezePuppet/repos\",\n" +
      "    \"events_url\": \"https://api.github.com/users/WheezePuppet/events{/privacy}\",\n" +
      "    \"received_events_url\": \"https://api.github.com/users/WheezePuppet/received_events\",\n" +
      "    \"type\": \"User\",\n" +
      "    \"site_admin\": false\n" +
      "  },\n" +
      "  \"html_url\": \"https://github.com/WheezePuppet/specnet\",\n" +
      "  \"description\": \"Social Primitives Experimental Cohort (SPEC) -- with social networks\",\n" +
      "  \"fork\": false,\n" +
      "  \"url\": \"https://api.github.com/repos/WheezePuppet/specnet\",\n" +
      "  \"forks_url\": \"https://api.github.com/repos/WheezePuppet/specnet/forks\",\n" +
      "  \"keys_url\": \"https://api.github.com/repos/WheezePuppet/specnet/keys{/key_id}\",\n" +
      "  \"collaborators_url\": \"https://api.github.com/repos/WheezePuppet/specnet/collaborators{/collaborator}\",\n" +
      "  \"teams_url\": \"https://api.github.com/repos/WheezePuppet/specnet/teams\",\n" +
      "  \"hooks_url\": \"https://api.github.com/repos/WheezePuppet/specnet/hooks\",\n" +
      "  \"issue_events_url\": \"https://api.github.com/repos/WheezePuppet/specnet/issues/events{/number}\",\n" +
      "  \"events_url\": \"https://api.github.com/repos/WheezePuppet/specnet/events\",\n" +
      "  \"assignees_url\": \"https://api.github.com/repos/WheezePuppet/specnet/assignees{/user}\",\n" +
      "  \"branches_url\": \"https://api.github.com/repos/WheezePuppet/specnet/branches{/branch}\",\n" +
      "  \"tags_url\": \"https://api.github.com/repos/WheezePuppet/specnet/tags\",\n" +
      "  \"blobs_url\": \"https://api.github.com/repos/WheezePuppet/specnet/git/blobs{/sha}\",\n" +
      "  \"git_tags_url\": \"https://api.github.com/repos/WheezePuppet/specnet/git/tags{/sha}\",\n" +
      "  \"git_refs_url\": \"https://api.github.com/repos/WheezePuppet/specnet/git/refs{/sha}\",\n" +
      "  \"trees_url\": \"https://api.github.com/repos/WheezePuppet/specnet/git/trees{/sha}\",\n" +
      "  \"statuses_url\": \"https://api.github.com/repos/WheezePuppet/specnet/statuses/{sha}\",\n" +
      "  \"languages_url\": \"https://api.github.com/repos/WheezePuppet/specnet/languages\",\n" +
      "  \"stargazers_url\": \"https://api.github.com/repos/WheezePuppet/specnet/stargazers\",\n" +
      "  \"contributors_url\": \"https://api.github.com/repos/WheezePuppet/specnet/contributors\",\n" +
      "  \"subscribers_url\": \"https://api.github.com/repos/WheezePuppet/specnet/subscribers\",\n" +
      "  \"subscription_url\": \"https://api.github.com/repos/WheezePuppet/specnet/subscription\",\n" +
      "  \"commits_url\": \"https://api.github.com/repos/WheezePuppet/specnet/commits{/sha}\",\n" +
      "  \"git_commits_url\": \"https://api.github.com/repos/WheezePuppet/specnet/git/commits{/sha}\",\n" +
      "  \"comments_url\": \"https://api.github.com/repos/WheezePuppet/specnet/comments{/number}\",\n" +
      "  \"issue_comment_url\": \"https://api.github.com/repos/WheezePuppet/specnet/issues/comments{/number}\",\n" +
      "  \"contents_url\": \"https://api.github.com/repos/WheezePuppet/specnet/contents/{+path}\",\n" +
      "  \"compare_url\": \"https://api.github.com/repos/WheezePuppet/specnet/compare/{base}...{head}\",\n" +
      "  \"merges_url\": \"https://api.github.com/repos/WheezePuppet/specnet/merges\",\n" +
      "  \"archive_url\": \"https://api.github.com/repos/WheezePuppet/specnet/{archive_format}{/ref}\",\n" +
      "  \"downloads_url\": \"https://api.github.com/repos/WheezePuppet/specnet/downloads\",\n" +
      "  \"issues_url\": \"https://api.github.com/repos/WheezePuppet/specnet/issues{/number}\",\n" +
      "  \"pulls_url\": \"https://api.github.com/repos/WheezePuppet/specnet/pulls{/number}\",\n" +
      "  \"milestones_url\": \"https://api.github.com/repos/WheezePuppet/specnet/milestones{/number}\",\n" +
      "  \"notifications_url\": \"https://api.github.com/repos/WheezePuppet/specnet/notifications{?since,all,participating}\",\n" +
      "  \"labels_url\": \"https://api.github.com/repos/WheezePuppet/specnet/labels{/name}\",\n" +
      "  \"releases_url\": \"https://api.github.com/repos/WheezePuppet/specnet/releases{/id}\",\n" +
      "  \"deployments_url\": \"https://api.github.com/repos/WheezePuppet/specnet/deployments\",\n" +
      "  \"created_at\": \"2019-02-13T17:14:24Z\",\n" +
      "  \"updated_at\": \"2019-05-08T18:23:51Z\",\n" +
      "  \"pushed_at\": \"2019-05-08T18:23:49Z\",\n" +
      "  \"git_url\": \"git://github.com/WheezePuppet/specnet.git\",\n" +
      "  \"ssh_url\": \"git@github.com:WheezePuppet/specnet.git\",\n" +
      "  \"clone_url\": \"https://github.com/WheezePuppet/specnet.git\",\n" +
      "  \"svn_url\": \"https://github.com/WheezePuppet/specnet\",\n" +
      "  \"homepage\": \"\",\n" +
      "  \"size\": 6099,\n" +
      "  \"stargazers_count\": 2,\n" +
      "  \"watchers_count\": 2,\n" +
      "  \"language\": \"Jupyter Notebook\",\n" +
      "  \"has_issues\": true,\n" +
      "  \"has_projects\": true,\n" +
      "  \"has_downloads\": true,\n" +
      "  \"has_wiki\": true,\n" +
      "  \"has_pages\": false,\n" +
      "  \"forks_count\": 0,\n" +
      "  \"mirror_url\": null,\n" +
      "  \"archived\": false,\n" +
      "  \"disabled\": false,\n" +
      "  \"open_issues_count\": 0,\n" +
      "  \"license\": null,\n" +
      "  \"forks\": 0,\n" +
      "  \"open_issues\": 0,\n" +
      "  \"watchers\": 2,\n" +
      "  \"default_branch\": \"master\",\n" +
      "  \"network_count\": 0,\n" +
      "  \"subscribers_count\": 3\n" +
      "}";
}