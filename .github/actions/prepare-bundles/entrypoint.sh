#!/bin/sh
set -eu

mkdir -p "${output_directory}"
date > "${output_directory}"/timestamp.txt
