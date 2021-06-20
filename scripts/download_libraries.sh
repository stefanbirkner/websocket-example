#!/bin/bash

# This script downloads all libraries specified in {workDir}/src/libraries.txt
# from Maven Central to the folder "{workDir}/lib". Each library is a single
# line in the input file with the format "group:name:version"
#
# Example:
# org.slf4j:slf4j-api:1.7.30

readonly TARGET_FOLDER="lib"
readonly FILE_LIST="$TARGET_FOLDER/urls.txt"

create_url_list()
{
  mkdir -p $TARGET_FOLDER
  while read library
  do
    add_url_for_library "$library"
  done < ./src/libraries.txt
}

add_url_for_library()
{
  local library=$1

  IFS=':' read -a parts <<< "$library"
  group=${parts[0]//\./\/}
  name=${parts[1]}
  version=${parts[2]}
  echo "https://repo1.maven.org/maven2/$group/$name/$version/$name-$version.jar" >> $FILE_LIST
}

download_libraries()
{
  wget --cut-dirs=10 \
    -i $FILE_LIST \
    --directory-prefix=$TARGET_FOLDER
}

delete_url_list()
{
  rm $FILE_LIST
}

create_url_list
download_libraries
delete_url_list
