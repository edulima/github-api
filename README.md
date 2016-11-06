###Java GitHub client

#####Project scope

Provide a HTTP API that given a user's GitHub username, retrieves the top five
public repositories owned by that user, ordered by size. API returns data as JSON.

#####Requirements

* Play Framework 2.5
* Java 1.8

#####Setting up

* Clone the repo: *git@github.com:edulima/github-api.git*
* In the project root run: ```activator run```. Play should take care of downloading/installing
all the dependencies required for the project to run.

Play's default page should be available on ```http://localhost:9000```

#####Accessing the API

```Dev: http://localhost:9000/resource/{git-hub-username}```

#####Deployment

This app has been deployed on Heroku which has been configured to automatic deploy
the latest code from Master.






