# clj-slack

A wrapper around the [Slack Java SDK](https://github.com/slackapi/java-slack-sdk).

## Usage

```clojure

require [slack.core :as slack :refer [post-msg!]]
(post-msg! "xoxb-494114936464-1996087116864-kNtMFXgLu27J7N8qQp3CIb9A" "team-galaxie" "test message")

s.core> 
```

```bash
clojure -Sdeps {:deps {:git/url "https://github.com/defsquare/clj-slack" :sha "8324315d6fb9e1dc7d44b366f3e03fbed2fb2c42"}} -m slack.core --token "xoxb-594114936464-1996087116864-kNtMFXgLu27J7N8qQp3CIb9B" --channel "team-galaxie" --message "another test message"
```

## License

Copyright © 2021 Jeremiegrodziski

Distributed under the Eclipse Public License version 1.0.
