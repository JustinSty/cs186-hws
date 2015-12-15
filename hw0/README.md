# Homework 0

In this assignment, you will set up the tools needed to submit assignments in CS 186.

This assignment is due **Monday, September 7** at **11:59 p.m. PST**.

## Instructions (brief)
1. If you don't have one yet, create a GitHub account.
1. Fill out the form [here](http://goo.gl/forms/WB4RjBnZSp) to register your account with our course. Note that you should do this **as soon as possible**; **you will not be able to submit any assignments until this step is complete**.
1. Create the file `done.txt` and commit it to your repository.
1. Submit this assignment by pushing your finished commit to the branch `release/hw0`.

# `git` and GitHub

`git` is a *version control* system, helping you track different versions of your code, synchronize them across different machines, and collaborate with others. [GitHub](https://github.com) is a site which supports this system, hosting it as a service.

We will be using both `git` and GitHub to submit assignments in this course. If you don't know much about `git`, we *strongly recommend* you to familiarize yourself with this system; you'll be spending a lot of time with it!

There are many guides to using `git` online - [here](http://git-scm.com/book/en/v1/Getting-Started) is a great one to read. Feel free to ask us other questions during our office hours.

## Creating and registering your GitHub account

If you don't yet have a GitHub account, create one by following the instructions [here](https://help.github.com/articles/set-up-git/).

**Afterwards, register that account with us by completing [this form](http://goo.gl/forms/WB4RjBnZSp). If you make a mistake filling it out, you can revisit this link to correct it. Please fill out this form by Monday September 7.**

# Submitting Assignments in CS 186

All assignments in CS 186 will be submitted by turning code in through GitHub by pushing the relevant commit to a specific branch, unless we say otherwise.

You have access to two repositories in the `berkeley-cs186` GitHub organization:

1. [A course repository which contains public assignment information](https://github.com/berkeley-cs186/course) which you can *only* read from. We will post assignments **and updates** here. Please check our course site and Piazza to keep up-to-date on changes to assignments.

1. A personal repository whose name corresponds to the last two characters in your `inst.eecs.berkeley.edu` login which only you can see and which you can read and write to. (e.g. If your login is `cs186-db`, then this repository will be called `db`.) You will be using this repository to submit your assignments. You must keep the contents of this repository secure: **remember that we expect you to adhere to course policy regarding collaboration and academic honesty**.

Each assignment resides in a different top-level directory. Each assignment has a `README` which contains instructions, as well as the necessary files for that particular assignment. (For instance, the instruction you are reading right now are located [here](http://github.com/berkeley-cs186/course/blob/master/hw0/README.md).)

Your personal repository currently should be empty.

## Setting up your repositories

You should first set up a local repository.

    $ cd ~

Clone your personal repository. It should be empty.

    $ git clone "https://github.com/berkeley-cs186/xx.git"

Enter the cloned repository, track the course repository and clone it.

    $ cd xx/
    $ git remote add course "https://github.com/berkeley-cs186/course.git"
    $ git pull course master

Push clone to your personal repository.

    $ git push origin master

Please keep in mind that you will have *two* remote repositories. `course` will be what we release as homework and project skeletons, and the other one will be your personal repository -- you can set this up as `personal`, `origin`, or whatever else you'd prefer. Make sure you try to `rebase` from and `push` to the right repositories by specifying the name when you run your git commands. 


## Receiving new assignments and assignment updates

We will release new assignments by registering them as commits in the official course repository. From time to time, it may also be necessary to receive updates to our assignments (even though we try to release them as "perfectly" as possible the first time). Assuming you set up the tracking correctly, you can simply run this following command to receive new assignments and/or updates:

    $ git pull course master

## Submitting assignments

To submit an assignment, push a branch named `release/<assignment-name>` to your personal repository, where `<assignment-name>` is the name of your assignment:

    $ git push origin master:release/hw0

Keep in mind that you must do this to make sure you've submitted your assignment correctly. You can confirm that you've submitted correctly checking that the branch `release/<assignment-name>` exists on GitHub.

We will grade the last commit you've pushed to `release/<assignment-name>`.

## An example: completing this assignment

If you are still learning how to use Git, you *must* read the short guide [Using Git](http://berkeley-cs61b.github.io/public_html/materials/guides/using-git.html) and especially the section on [Remote Repositories](http://berkeley-cs61b.github.io/public_html/materials/guides/using-git.html#f-remote-repositories).

If you haven't already, first clone your personal repository from GitHub:

    $ cd ~
    $ git clone https://github.com/berkeley-cs186/xx.git
    $ cd xx/
    $ git remote add course https://github.com/berkeley-cs186/course.git
    $ git pull course master

Move into the hw0 directory in your repository and create the file `done.txt`. Register this change with a commit and push it into GitHub's servers:

    $ cd ~/xx/hw0/
    $ touch done.txt
    $ git add done.txt
    $ git commit -m 'created homework file'
    $ git push origin master

Occasionally we might release an update to assignment files (make sure you add the course remote as detailed above first):

    $ git pull course master

Finally, when you're satisfied with your submission, submit it

    $ git push origin master:release/hw0

and you're finished!

