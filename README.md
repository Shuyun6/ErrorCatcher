# ErrorCatcher
catch android global errors and show a friendly activity after finishing

Build in the begining of your application to catch errors
```Java
CatcherHandler.getInstance(getApplicationContext())
                .addActivity(this)
                .setErrorActivity(ErrorPagerActivity.class)
                .setDelayTimeBeforeFinish(1200)
                .build();
```
Before:
![](https://github.com/Shuyun6/ErrorCatcher/blob/master/before.png)
After:
![](https://github.com/Shuyun6/ErrorCatcher/blob/master/after.png)
