#pragma once

#include <cstddef>

namespace embedded_assets {
struct Image {
    const unsigned char* data;
    std::size_t size;
};

extern const Image drawIcon;
extern const Image playIcon;
extern const Image questionIcon;
extern const Image userIcon;
extern const Image playOn;
extern const Image playOff;
extern const Image blackLogo;
extern const Image refKey;
extern const Image refPaste;
extern const Image refTelegram;
}  // namespace embedded_assets
