<p align="center">
<img src="https://i.imgur.com/gwI0AqH.png" alt="Skywalx Logo" width="350" height="350"/>
</p>
<h1 align="center">Simple Player Authentication</h1>
<p align="center">
<i>Made by Skywalx</i>
</p>
<p align="center">
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
- Hashing of the accounts password using Argon2, Bcrypt or Scrypt.
- Storage of accounts within `accounts.yaml` in the Plugin folder

# :page_with_curl: Commands

| Usage                           | Description | Permission |
|---------------------------------|-------------|------------|
| /register [password] [password] |             | None       |
| /unregister [password]          |             | None       |
| /login [password]               |             | None       |
| /logout                         |             | None       |

# :wrench: Future Improvements

- More storage options such as: MySQL, PostgreSQL and MSSQL

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

- Yes you can! The plugin is license under the [MIT license](LICENSE) which allows you to fork this project and reuse it
  even for commercial use. The only restriction is that you cannot remove Skywalx copyright markings within the code.
  Otherwise, feel free to use to your liking!