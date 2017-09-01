# ErrorCatcher
catch andorid global errors and show a friendly activity after finishing

Build in the begining of your application to catch errors
```Java
CatcherHandler.getInstance(getApplicationContext())
              .setActivities(app.getListOfActivities())
              .setErrorActivity(ErrorPagerActivity.class)
              .setDelayTime(1200)
              .build();
```
Before:
![](https://github.com/Shuyun6/ErrorCatcher/blob/master/old.png)  
After:
![](https://github.com/Shuyun6/ErrorCatcher/blob/master/oops.png)  
