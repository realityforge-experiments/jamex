require 'spec'

DEFAULT_BUILDR_DIR=File.expand_path(File.dirname(__FILE__) + '/../../buildr')
BUILDR_DIR=ENV['BUILDR_DIR'] || DEFAULT_BUILDR_DIR

unless File.exist?("#{BUILDR_DIR}/buildr.gemspec")
  raise "Unable to find buildr.gemspec in #{BUILDR_DIR == DEFAULT_BUILDR_DIR ? 'guessed' : 'specified'} $BUILD_DIR (#{BUILDR_DIR})"
end

require 'rubygems'

# For testing we use the gem requirements specified on the buildr.gemspec
Gem::Specification.load(File.expand_path("#{BUILDR_DIR}/buildr.gemspec", File.dirname(__FILE__))).
    dependencies.each { |dep| gem dep.name, dep.requirement.to_s }


# hook into buildr's spec_helpers load process
unless defined?(SpecHelpers)
  module SandboxHook
    def SandboxHook.included(spec_helpers)
      $LOAD_PATH.unshift(File.dirname(__FILE__))
      $LOAD_PATH.unshift(File.join(File.dirname(__FILE__), '..', 'lib'))
      require 'buildr_bnd'
    end
  end

  require "#{BUILDR_DIR}/spec/spec_helpers.rb"
end