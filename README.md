<p align="center">
<img src="https://i.imgur.com/gwI0AqH.png" alt="Skywalx Logo" width="350" height="350"/>
</p>
<h1 align="center">Simple Player Authentication</h1>
<p align="center">
<i>Made by Skywalx</i>
</p>
<p align="center">
<a href="https://github.com/Skywalx/simple-player-authentication/actions/workflows/build.yml" target="_blank">
    <img src="https://github.com/Skywalx/simple-player-authentication/actions/workflows/build.yml/badge.svg" alt="Build"/>
</a>
<a href="https://github.com/Skywalx/simple-player-authentication/actions/workflows/release.yml" target="_blank">
    <img src="https://github.com/Skywalx/simple-player-authentication/actions/workflows/release.yml/badge.svg" alt="Release"/>
</a>
<a href="https://github.com/Skywalx/simple-player-authentication/releases">
  <img src="https://img.shields.io/github/v/release/Skywalx/simple-player-authentication" alt="Release">
</a>
<a href="https://feedback.minecraft.net/hc/en-us/sections/360001186971-Release-Changelogs">
  <img src="https://img.shields.io/badge/versions-1.17.1%20--%201.19.2-blue" alt="Versions">
</a>
<a href="https://github.com/Skywalx/item-weight-lib/blob/main/LICENSE" target="_blank">
    <img src="https://img.shields.io/github/license/Skywalx/item-weight-lib" alt="License"/>
</a>
</p>


---

**Website**: https://www.skywalx.com

**Discord**: https://discord.skywalx.com/

**Business E-mail**: business@skywalx.com

---

# :white_check_mark: Features

- Ability to register and unregister an account.
- Ability to login into and logout from an existing account.
- Hashing of the accounts password using Argon2
- Storage of accounts within `accounts.yaml` in the Plugin folder
- Ability to configure which player actions are allowed before authentication

# :page_with_curl: Commands

| Usage                                                            | Description                                                | Permission |
|------------------------------------------------------------------|------------------------------------------------------------|------------|
| /register [password] [password]                                  | Command to register a new account.                         | None       |
| /unregister [password]                                           | Command to unregister an existing account.                 | None       |
| /login [password]                                                | Command to login into an account.                          | None       |
| /logout                                                          | Command to logout from an account.                         | None       |

# :wrench: Future Improvements

- More hashing algorithms such as: Bcrypt and Scrypt
- More storage options such as: MySQL, MongoDB
- More authentication storage options, to share authenticated state between servers with Redis, REST
- Notify user every x time that /login should be used when not authenticated yet
- 2FA, both inclusive and exclusive option (configurable) with credentials login
- Allow defining a regex expression in the config for password policy
- Create an option that shows login screen on login

:exclamation: Attention: This is not a decisive list! There is no roadmap for this Minecraft plugin so new features
might not have been listed here or features listed here might not be implemented! :exclamation:

# :question: FAQ

**Isn't this plugin supporting the use of 'online-mode=false' and 'cracked' Minecraft accounts which is an illegitimate
use of Minecraft ?**

- No! Although these types of 'authentication' plugins were famous for being used in these scenarios, this is not built
  for this
  purpose, and we certainly do not condemn it.

**Is this the same plugin as the AuthMe plugin ?**

- No! Although the concept is very similar, we've built this plugin with custom features and have no affiliation with
  the AuthMe plugin.

**Is there a release window with planned features ?**

- As of the time of writing, this plugin is maintained on an irregular basis and new features are mostly implemented by
  suggestions or ideas that arise within Skywalx itself.

**Can I use the source code of this plugin to adapt to my own liking ?**

- Yes you can! The plugin is licensed under the [MIT license](LICENSE) which allows you to fork this project and reuse
  it even for commercial use. The only restriction is that you cannot remove Skywalx copyright markings within the code.
  Otherwise, feel free to use to your liking!
