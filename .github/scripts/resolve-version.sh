#!/usr/bin/env bash
set -euo pipefail

tag="${1:-}"
pattern='^v(0|[1-9][0-9]*)\.(0|[1-9][0-9]*)\.(0|[1-9][0-9]*)$'

if [[ ! "$tag" =~ $pattern ]]; then
    printf '%s\n' "Invalid release tag '$tag'. Expected vMAJOR.MINOR.PATCH without leading zeroes." >&2
    exit 64
fi

printf '%s.%s.%s\n' "${BASH_REMATCH[1]}" "${BASH_REMATCH[2]}" "${BASH_REMATCH[3]}"
