#include "embedded_images.h"

#include "draw_icon.h"
#include "play_icon.h"
#include "q_icon.h"
#include "user_icon.h"
#include "play_on.h"
#include "play_off.h"
#include "ref/black_logo_png.h"
#include "ref_key_png.h"
#include "ref_paste_png.h"
#include "ref_telegram_png.h"

namespace embedded_assets {
const Image drawIcon{draw_icon_png, sizeof(draw_icon_png)};
const Image playIcon{play_icon_png, sizeof(play_icon_png)};
const Image questionIcon{q_icon_png, sizeof(q_icon_png)};
const Image userIcon{user_icon_png, sizeof(user_icon_png)};
const Image playOn{play_on_png, sizeof(play_on_png)};
const Image playOff{play_off_png, sizeof(play_off_png)};
const Image blackLogo{black_logo_png, sizeof(black_logo_png)};
const Image refKey{ref_key_png, sizeof(ref_key_png)};
const Image refPaste{ref_paste_png, sizeof(ref_paste_png)};
const Image refTelegram{ref_telegram_png, sizeof(ref_telegram_png)};
}  // namespace embedded_assets
