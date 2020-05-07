# Bulwark FM Glue
<details>
<summary>🌅 Background</summary>

**Problem**

Anchor FM is the easiest way to start a podcast. But ease comes at the cost of SEO juice. 
The one click distribute option provided by Anchor publishes to their channels. 

This makes is impossible to move to another provider. This also puts the host in a compromised position, since access to audience is controlled by Anchor.

Although Anchor allows for an option to submit feed manually, essentially returning control to the podcaster, there are still links that point back to Anchor services.

**Solution**

We wanted to build a JAMStack solution for hosting a Podcast. But since Anchor exposes a feed, we decided to use Anchor as a backend instead of rolling out our own.

We transform Anchor's feed by replacing backlinks to anchor.fm with a custom url (your podcast website). We also create json files for channel meta, episode index and individual episodes. You can then easily use this json API to spin up your own sites, with all media hosted at Anchor, and all SEO juice sent to your domain.

</details>

# Getting Started
This repository uses Github Actions to convert your Podcast RSS into json. This json is exposed via Github Pages. You can trigger using Issues or a web hook. More on this later. 

*Note: This document uses the terms "feed", "rss feed", "anchor's rss feed" interchangebly.*

## Clone this template
Clone this repo to your account. 

## Setup Secrets
The scripts need your `ANCHOR_USERNAME` and `SITE_BASE` address to transform the feed.
The `SITE_BASE` should have no trailing slash, ex: `https://madebybulwark.fm`

## Open a build placeholder issue
The first issue on the repository is like a terminal for triggering builds. 
By commenting `build` on this issue, you'll trigger the process that fetches the feed and transforms it into json.

You can also configure the Github workflow file to setup a cron.
TODO: Perhaps we should setup a cron trigger, maybe once per day

## Done
Well, not quite. This glue repository is setup and will transform your feed, but you still need to make a static site to consume this feed. Please check our reference implementation at https://github.com/bulwarkfm/static-site and set of reusable components at https://github.com/bulwarkfm/ui.

# Future Goals

## Moving Media to Personal Storage Provider
We plan to move audio and video files to an S3 compatable storage provider in future. This feature will obviously be an opt-in.

## Replacing Anchor with Netlify CMS
Think about how to store media. And how to make the process seamless and possibly free.

# Legal 
We are not lawyers and are not responsible if you use this tool and Anchor sues you. This repo is only meant for the brave. You might want to read [Anchor's TOS](https://anchor.fm/tos).

# License
**MIT**
Copyright 2020, Anand Chowdhary, Deepak Gupta, Shivek Khurana

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.