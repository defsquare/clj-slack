# clj-slack

A wrapper around the [Slack Java SDK](https://github.com/slackapi/java-slack-sdk).

## Usage

```clojure

require [slack.core :as slack :refer [post-msg!]]
(post-msg! "xoxb-494114936464-xxx" "team-xxx" "test message")

s.core> 
```

```bash
clj -Sdeps '{:deps {clj-slack/clj-slack {:git/url "https://github.com/defsquare/clj-slack" :sha "5f1703f20a56dd04f9d8cc2db951f8073245cc26"}} :aliases {:post-msg {:exec-fn slack.core/post-msg!}}}' -Xpost-msg :token '"xoxb-494114936464-199xxxx"' :channel '"team-xxx"' :message '"hello world"'

```

## License

Copyright © 2021 Defsquare

Distributed under the Eclipse Public License version 1.0.
