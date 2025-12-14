#!/bin/bash

# Generate Tophat Installation Link Script
# This script creates a Tophat URL for installing Android APKs
# Usage: ./generate-tophat-link.sh <apk_url>

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to URL encode
urlencode() {
    local string="${1}"
    local strlen=${#string}
    local encoded=""
    local pos c o

    for (( pos=0 ; pos<strlen ; pos++ )); do
        c=${string:$pos:1}
        case "$c" in
            [-_.~a-zA-Z0-9] ) o="${c}" ;;
            * ) printf -v o '%%%02x' "'$c"
        esac
        encoded+="${o}"
    done
    echo "${encoded}"
}

# Check if APK URL is provided
if [ -z "$1" ]; then
    echo -e "${RED}Error: APK URL is required${NC}"
    echo "Usage: $0 <apk_url>"
    echo "Example: $0 https://example.com/app.apk"
    exit 1
fi

APK_URL="$1"

# Validate URL format
if [[ ! "$APK_URL" =~ ^https?:// ]]; then
    echo -e "${RED}Error: Invalid URL format. Must start with http:// or https://${NC}"
    exit 1
fi

# Generate Tophat URL
# Format: tophat://install/http?url=<encoded_url>
ENCODED_URL=$(urlencode "$APK_URL")
TOPHAT_URL="tophat://install/http?url=${ENCODED_URL}"

# Also create HTTP version for GitHub/PR comments
HTTP_TOPHAT_URL="http://localhost:29070/install/http?url=${ENCODED_URL}"

# Output results
echo ""
echo -e "${BLUE}=========================================="
echo -e "📱 TOPHAT INSTALLATION LINK GENERATED"
echo -e "==========================================${NC}"
echo ""
echo -e "${GREEN}Tophat URL (for direct use):${NC}"
echo "$TOPHAT_URL"
echo ""
echo -e "${GREEN}HTTP URL (for GitHub/PR comments):${NC}"
echo "$HTTP_TOPHAT_URL"
echo ""
echo -e "${YELLOW}Markdown format (for PR comments):${NC}"
echo "[Install with Tophat]($HTTP_TOPHAT_URL)"
echo ""
echo -e "${BLUE}==========================================${NC}"
echo ""

# Copy to clipboard if on macOS
if [[ "$OSTYPE" == "darwin"* ]]; then
    echo "$TOPHAT_URL" | pbcopy
    echo -e "${GREEN}✓ Tophat URL copied to clipboard${NC}"
    echo ""
fi

# Save to file
OUTPUT_FILE="tophat-link.txt"
echo "$TOPHAT_URL" > "$OUTPUT_FILE"
echo "$HTTP_TOPHAT_URL" >> "$OUTPUT_FILE"
echo -e "${GREEN}✓ Link saved to ${OUTPUT_FILE}${NC}"
echo ""


