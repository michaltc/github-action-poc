#!/bin/sh
set -eu

mkdir -p "${INPUT_OUTPUT_DIRECTORY}"
date > "${INPUT_OUTPUT_DIRECTORY}"/timestamp.txt
